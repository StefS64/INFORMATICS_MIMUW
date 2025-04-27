#include "network_manager.hpp"

#define BUFFER_SIZE 65507 // Maximal size of package for my MTU

// #define DEBUG
#ifdef DEBUG
#include <iomanip>  // for std::setw and std::setfill
#include <bitset>
#define RESET   "\x1b[0m"
#define RED     "\x1b[31m"
#define GREEN   "\x1b[32m"
#define YELLOW  "\x1b[33m"
#define BLUE    "\x1b[34m"
#define MAGENTA "\x1b[35m"
#define CYAN    "\x1b[36m"
#endif
static char buffer[BUFFER_SIZE];

NetworkManager::NetworkManager(int socket_fd, NodeConfig& data) : socket_fd(socket_fd), data(data), state(UNSYNCHRONIZED) {}

// Development functions mostly print and debugs.
#ifdef DEBUG
void printBufferDebug(size_t length) {
  std::cout << "Buffer (" << length << " bytes): ";
  for (size_t i = 0; i < length; ++i) {
    std::cout << "0x"
      << std::hex << std::setw(2) << std::setfill('0')
      << (static_cast<unsigned int>(static_cast<unsigned char>(buffer[i]))) << " ";
  }
  std::cout << std::dec << std::endl; // reset to decimal just in case
}
#endif
/* Block of fuctions used to send messages.
*
*/
// Function for sending single 1 byte Message. Not suitable for HELLO_REPLY, SYNC_START, DELAY_RESPONSE, TIME.
void NetworkManager::sendMessage(const address_info& peer_addr, MessageType type) {
  if (type == HELLO_REPLY || type == SYNC_START || type == DELAY_RESPONSE || type == TIME) {
    fatal("Wrong message type for sendMessage");
  }

  memset(buffer, 0, sizeof(buffer));

  buffer[0] = type;
  sockaddr_in send = peer_addr.toSockaddrIn(); 
  ssize_t sent_length = sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&send, sizeof(send));

  if (sent_length < 0) {
    syserr("sendto hello %d", type);
  }
  else if ((size_t) sent_length != 1) {
    syserr("incomplete sending");
  }
#ifdef DEBUG
  std::string typeStr;
  switch (type) {
    case HELLO:        typeStr = "HELLO"; break;
    case CONNECT:      typeStr = "CONNECT"; break;
    case ACK_CONNECT:  typeStr = "ACK_CONNECT"; break;
    case DELAY_REQUEST: typeStr = "DELAY_REQUEST"; break;
    default:           typeStr = "UNKNOWN"; break;
  }
  std::cout<< CYAN << "Sent " << typeStr << " to " << peer_addr << "\n" << RESET;
#endif
}

// Parse helper for HELLO_REPLY only more complicated reply function 
bool _parseHelloReply(size_t len, std::vector<address_info>& peers, const sockaddr_in& sender, const std::set<address_info>& own_address ){
  if (len < 2) return false;
  uint8_t count = static_cast<uint8_t>(buffer[1]);
  size_t index = 2;
#ifdef DEBUG
  std::cout <<"count: " << static_cast<int>(count) << std::endl;
#endif
  for (uint8_t i = 0; i < count; ++i) {
    if (index >= len) return false;
    uint8_t addr_len = static_cast<uint8_t>(buffer[index++]);

#ifdef DEBUG
    std::cout <<"addr_len: " << static_cast<int>(addr_len) << std::endl;
#endif
    if (index + addr_len + 2 > static_cast<size_t>(len)) return false;

    in_addr_t ip = 0;
    if (addr_len == sizeof(in_addr_t)) {
      std::memcpy(&ip, buffer + index, addr_len);
    } else {
      return false;
    }
    index += addr_len;

    // read 2‑byte big‑endian port
    uint16_t port = 0;
    std::memcpy(&port, buffer + index, 2);
    index += 2;

    //Validation of sin_port
    if (port == 0) {
      error_msg(buffer, len);
      return false;
    }

    address_info dummy = address_info(ip, port);

    if (ip == INADDR_ANY || ip == INADDR_BROADCAST || dummy == sender || own_address.find(dummy) != own_address.end()) {
      error_msg(buffer, len);
      return false;
    }

    peers.emplace_back(ip, port);
  }

  return true;
}
// Functions for handling messages while connecting.
void NetworkManager::handleHello(const sockaddr_in& sender, socklen_t sender_len) {
  buffer[0] = HELLO_REPLY;
  size_t index = 2;

  size_t number_of_connections = 0;

  for(auto& connection : connections) {
    if (connection != sender) {// Make sure we don't send the sender to himself.
      number_of_connections++;
      std::string serialized = connection.serialize();
      std::copy(serialized.begin(), serialized.end(), buffer + index);
      index += serialized.size();
    }
    if (index > BUFFER_SIZE - 2) {
      error_msg(buffer, index);// Ignoring send message to large.
      return;
    }
  }

  buffer[1] = number_of_connections;
  size_t length = index;
  ssize_t sent_length = sendto(socket_fd, buffer, length, 0,
                               (struct sockaddr*)&sender, sender_len);
  if (sent_length < 0) {
    syserr("sendto HELLO_REPLY");
  } else if ((size_t) sent_length != length) {
    syserr("incomplete sending");
  }
  addConnection(sender, CONFIRMED); // Add the sender.
}
// Parse peer list and send CONNECT messages.
void NetworkManager::handleHelloReply(const sockaddr_in& sender, ssize_t len) {
  // Check if the sender was sent to HELLO. 
  if (data.getPeerAddress() != sender || _getFlag(sender) != UNKNOWN) {
#ifdef DEBUG
    printBufferDebug(len);
#endif
    error_msg(buffer,len);
    return;
  }

  std::vector<address_info> peers;
#ifdef DEBUG
  printBufferDebug(len);
#endif

  if (!_parseHelloReply(len, peers, sender, data.getLocalAddress())) {
    error_msg(buffer, len);
    return;
  }

  for (const auto& peer : peers) {
    if (_getFlag(peer) == UNKNOWN) {
      addConnection(peer, UNCONFIRMED);
      sendMessage(peer, CONNECT);// Send CONNECT to each we didn't already know.
    }
  }

  addConnection(sender, CONFIRMED);
}

// Main node loop.
void NetworkManager::runNode() {
  if (data.isPeerPresent()) {
    sendMessage(data.getPeerAddress(), HELLO);
  }

  while (true) {
    handleSyncSending();
    handleIncomingMessage();
  }
}


void NetworkManager::handleIncomingMessage() {
  memset(buffer, 0, sizeof(buffer));
  struct sockaddr_in sender_addr;
  socklen_t sender_len = sizeof(sender_addr);
  int flags = 0;

  ssize_t len = recvfrom(socket_fd, buffer, BUFFER_SIZE, flags,
                         (struct sockaddr*)&sender_addr, &sender_len);
  uint64_t recv_time = GlobalClock::now();
#ifdef DEBUG
  std::cout << "[Node] Program uptime: " << GlobalClock::now() << " ms\n"; 
#endif // DEBUG 
  if (len < 0) {
    if (errno == EAGAIN || errno == EWOULDBLOCK) {
      // No data arrived in timeout period.
      return;
    }
#ifdef DEBUG
    std::cout <<"recvfrom error or timeout" << std::endl;
#endif // DEBUG
    syserr("recvfrom");
    return;
  } else if (len == 0) {
    syserr("incomplete datagram");
    return;
  }

  // Parse the message type.
  uint8_t message_type = buffer[0];

  switch (message_type) {
    case HELLO:
#ifdef DEBUG
    std::cout << "Received HELLO from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif 
    handleHello(sender_addr, sender_len);
    break;
    case HELLO_REPLY:
#ifdef DEBUG
    std::cout << "Received HELLO_REPLY from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif 
    handleHelloReply(sender_addr, len);
    break;
    case CONNECT:
#ifdef DEBUG
    std::cout << "Received CONNECT from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif 
    // Respond with ACK_CONNECT doesn't matter who he is.
    addConnection(sender_addr, CONFIRMED);
    sendMessage(sender_addr, ACK_CONNECT);
    break;
    case ACK_CONNECT:
#ifdef DEBUG
    std::cout << "Received ACK_CONNECT from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
      std::cout << _getFlag(sender_addr) << std::endl;
#endif 
    if (_getFlag(sender_addr) == UNCONFIRMED) { 
      addConnection(sender_addr, CONFIRMED);
    } else {
      error_msg(buffer, len);
    }
    break;
    case SYNC_START:
#ifdef DEBUG
    std::cout << "Received SYNC_START from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif
    handleSyncStart(sender_addr, len, recv_time);
    break;
    case DELAY_REQUEST:
#ifdef DEBUG
    std::cout << "Received DELAY_REQUEST from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif
    handleDelayRequest(sender_addr);
    break;
    case DELAY_RESPONSE:
#ifdef DEBUG
    std::cout << "Received DELAY_RESPONSE from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif
    handleDelayResponse(sender_addr, len, recv_time);
    break;
    case LEADER:
#ifdef DEBUG
    std::cout << "Received LEADER from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif
    handleLeader(len);
    break;
    case GET_TIME:
#ifdef DEBUG
    std::cout << "Received GET_TIME from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif
    sendSyncWithData(address_info(sender_addr), TIME, true);
    break;
    default:
      error_msg(buffer, len);
      break;
  }
}

bool NetworkManager::addConnection(const sockaddr_in& sender, short flag) {
  address_info addr_short = address_info(sender, flag);
  return addConnection(addr_short, flag);
}
// Functions to manage saved connections.
// IMPORTANT, connections are confirmed if and only if they received a message from that adress.
bool NetworkManager::addConnection(const address_info& addr_short, short flag) {
  auto connection = connections.find(addr_short);

  if (connection == connections.end()) {
    connections.insert(addr_short);
  } else if ((flag != connection->flags && flag != UNCONFIRMED)
    || (connection->flags >= SYNCHRONIZING && flag >= SYNCHRONIZING)) {
    connection->flags = flag;// Only change if node is not already sychronized. 
  } else {
    // The flag has not been set.
    return false;
  }
  return true;
}

short NetworkManager::_getFlag(const sockaddr_in& connection) {
  return _getFlag(address_info(connection));
}

short NetworkManager::_getFlag(const address_info& connection) {
  auto iter = connections.find(connection);
  if (iter == connections.end()) {
    return -1;
  }
  return iter->flags;
}

// Synchronization functions.
void NetworkManager::sendSyncWithData(const address_info& peer_addr, MessageType type, bool offset_flag) {
#ifdef DEBUG
  if (type != TIME && (type <= ACK_CONNECT || DELAY_RESPONSE < type || DELAY_REQUEST == type)) {
    fatal("Wrong message type!!");
  }

  std::string typeStr;
  switch (type) {
    case DELAY_RESPONSE:        typeStr = "DELAY_RESPONSE"; break;
    case TIME:      typeStr = "TIME"; break;
    case SYNC_START:  typeStr = "SYNC_START"; break;
    default:           typeStr = "UNKNOWN"; break;
  }
  std::cout << CYAN << "Sent " << typeStr << " to " << peer_addr << "\n"<< RESET;
#endif
  memset(buffer, 0, sizeof(buffer));

  buffer[0] = type;
  buffer[1] = data.getSyncLevel();
  uint64_t timestamp = GlobalClock::now();
#ifdef DEBUG
  std::cout <<"local time: " << timestamp << " offset: "<< GlobalClock::getOffset() <<" global time: " << timestamp - GlobalClock::getOffset() << std::endl;
#endif 
  if (offset_flag) {
    timestamp -= GlobalClock::getOffset();
#ifdef DEBUG
    GlobalClock::print_times();
    std::cout <<"IMPORTANTE" << timestamp << std::endl;
#endif
  }

  timestamp = htobe64(timestamp);
  std::memcpy(buffer + 2, &timestamp, 8);
  sockaddr_in send = peer_addr.toSockaddrIn(); 

  ssize_t sent_length = sendto(socket_fd, buffer, 10, 0,
                               (struct sockaddr*)&send, sizeof(send));
  if (sent_length < 0) {
    syserr("sendto");
  } else if ((size_t) sent_length != 10) {
    syserr("incomplete sending");
  }
}

bool NetworkManager::validSyncRequest(const sockaddr_in& sender_addr,
                                      uint8_t synchronized) {
#ifdef DEBUG
  std::cout << GREEN << GlobalClock::now() - synced_time << RESET << std::endl;
#endif
  if ((state == SYNCHRONIZED && data.getSyncAddr() == sender_addr) &&
    ((data.getSyncLevel() <= synchronized) ||
    (GlobalClock::now() - synced_time >= 4*FIVE_SECONDS))) {
    data.setSyncLevel(255);
    GlobalClock::resetOffset();
    state = UNSYNCHRONIZED;
    return false;
  }
  return true;
}
// Child node.
void NetworkManager::handleSyncStart(const sockaddr_in& sender_addr,
                                     ssize_t len, uint64_t recv_time) {
  short flag = _getFlag(sender_addr);

  if (len != 10 || flag == UNCONFIRMED || flag == UNKNOWN) {
    error_msg(buffer, len);
    return;
  }

  uint8_t synchronized = buffer[1];

  if (synchronized >= 254 || state == LEADING) {
    return;
  }

  uint64_t time;
  memcpy(&time, buffer + 2, 8);
  time = be64toh(time);

  if (!validSyncRequest(sender_addr, synchronized)) {
    error_msg(buffer, len);
    return;
  }

  if ((state == SYNCHRONIZED && data.getSyncAddr() == sender_addr
    && synchronized < data.getSyncLevel()) ||
    (state != SYNCHRONIZED && synchronized + 2 <= data.getSyncLevel()) ) {
    GlobalClock::saveT(1, time);
    GlobalClock::saveT(2, recv_time);
    data.setSyncAddr(address_info(sender_addr), synchronized);
    GlobalClock::saveT(3);
    sendMessage(sender_addr, DELAY_REQUEST);
  } 
}

bool NetworkManager::checkTime(const sockaddr_in& sender_addr) {
  auto iter = connections.find(address_info(sender_addr));
  return (GlobalClock::now() - iter->sync_time) <= 2 * FIVE_SECONDS;
}

// Father node.
void NetworkManager::handleDelayRequest(const sockaddr_in& sender_addr) {
  short flag = _getFlag(sender_addr);
  if (flag != SYNCHRONIZING || !checkTime(sender_addr)) {
#ifdef DEBUG
    std::cout << flag <<" time check: "<<checkTime(sender_addr) << std::endl;
#endif
    error_msg(buffer, 1);
    return;
  }
  addConnection(sender_addr, SYNCED); 
  sendSyncWithData(address_info(sender_addr), DELAY_RESPONSE, state == SYNCHRONIZED);
}

//Child node.
void NetworkManager::handleDelayResponse(const sockaddr_in& sender_addr,
                                         ssize_t len, uint64_t recv_time) {
  if (len != 10) {
    error_msg(buffer, len);
    return;
  }

  uint8_t synchronized = buffer[1];

  if (data.getSyncLevelSyncWith() != synchronized) {
    error_msg(buffer, len);
    return;
  }

  if (!validSyncRequest(sender_addr, synchronized)) {
    error_msg(buffer, len);
    return;
  }
  
  uint64_t time;
  memcpy(&time, buffer + 2, 8);
  time = be64toh(time);

  if (!validSyncRequest(sender_addr, synchronized) || GlobalClock::getT3() >= time) {
    error_msg(buffer, len);
    return;
  }

  if ((data.getSyncAddr() == sender_addr) && (state != LEADING)) {
    GlobalClock::saveT(4, time);
    GlobalClock::calcOffset();
    synced_time = GlobalClock::now();// Global sync to peer.
    state = SYNCHRONIZED;
    data.setSyncLevel(synchronized + 1);
  } else {
    error_msg(buffer, len);
    return;
  }
}

void NetworkManager::handleLeader(ssize_t len) {
  if (len != 2) {
    error_msg(buffer, len);
    return;
  }

  uint8_t flag = buffer[1];

#ifdef DEBUG
  std::cout << GREEN << int(flag) <<" : "<<int(state) << RESET <<std::endl;
#endif
  if (state == LEADING && int(flag) == 255) {
    data.setSyncLevel(255);
    state = STOPLEADING;
    return;
  }

  if (state != LEADING && flag == 0) {
    data.setSyncLevel(0);
    state = LEADING;
    return;
  }

  error_msg(buffer, len);
}

void NetworkManager::handleSyncSending() {
#ifdef DEBUG
  std::cout<< BLUE <<"handling sending" << RESET <<std::endl;
#endif
  if (state == STOPLEADING) {
    bool finished = true;
    for (auto& connection : connections) {
      if(connection.flags == SYNCED || connection.flags == SYNCHRONIZING) {
        if (GlobalClock::now() - connection.sync_time > FIVE_SECONDS) {
          connection.flags = CONFIRMED;
        } else {
          finished = false;
        }
      } 
    }

    if (finished) {
      state = UNSYNCHRONIZED; 
    }
    return;
  }

  address_info addr =  data.getSyncAddr();
  sockaddr_in dummy = addr.toSockaddrIn();

  if (data.getSyncLevel() >= 254 || !validSyncRequest(dummy, 0)) {
#ifdef DEBUG
    std::cout << YELLOW << "High sync level" << RESET <<std::endl;
#endif
    return;
  }

  if (GlobalClock::now() - set_leader < TWO_SECONDS) {
    return;
  }

  for (auto& connection : connections) {
    #ifdef DEBUG
    std::cout << MAGENTA <<  connection << RESET <<std::endl;
    #endif
    if (GlobalClock::now() - connection.sync_time > FIVE_SECONDS) {
      connection.sync_time = GlobalClock::now();
#ifdef DEBUG
      std::cout <<"send sync" <<connection.sync_time <<" " << FIVE_SECONDS << " " << GlobalClock::now()<<'\n';
#endif
      if (state == SYNCHRONIZED && connection != addr) {
        sendSyncWithData(connection, SYNC_START, true);
      } else {
        sendSyncWithData(connection, SYNC_START, false);
      }
      connection.flags = SYNCHRONIZING;
    } 
  } 
}
