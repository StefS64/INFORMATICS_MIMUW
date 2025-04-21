#include "network_manager.hpp"

#define BUFFER_SIZE 65507 // Maximal size of package for my MTU

#define DEBUG
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

NetworkManager::NetworkManager(int socket_fd, NodeConfig& data) : socket_fd(socket_fd), data(data), state(UNSYNCHRONIZED) {
  #ifdef DEBUG
  std::cout << "NetworkManager up: " << GlobalClock::now() << " ms\n";
  #endif // DEBUG
}
// Development functions mostly print and debugs.
void printBuffer(size_t length) {
  std::cout << "Buffer (" << length << " bytes): ";
  for (size_t i = 0; i < length; ++i) {
    std::cout << "0x"
      << std::hex << std::setw(2) << std::setfill('0')
      << (static_cast<unsigned int>(static_cast<unsigned char>(buffer[i]))) << " ";
  }
  std::cout << std::dec << std::endl; // reset to decimal just in case
}
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
  std::cout <<"debug send" << std::endl;
  if (sent_length < 0) {
    syserr("sendto hello %d", type);
  }
  else if ((size_t) sent_length != 1) {
    fatal("incomplete sending");
  }
  std::cout <<"debug send end" << std::endl;
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
bool _parseHelloReply(size_t len, std::vector<address_info>& peers) {
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
#ifdef DEBUG
    std::cout << ntohs(port) << std::endl;
#endif
    //Validation of sin_port
    if (port == 0) {
      error("Port can't be zero");
      return false;
    }

    if (ip == INADDR_ANY || ip == INADDR_BROADCAST) {
      error("Ip is invalid not specific adress");
      return false;
    }

    peers.emplace_back(ip, port);
  }

  return true;
}
// TODO check which funcitons don't need to be private and can be hidden.
/* Functions for handling messages while connecting */
void NetworkManager::handleHello(const sockaddr_in& sender, socklen_t sender_len) {
  int ind = _findSockaddr(sender); // TODO check this.
  if(ind >= 0) {
    error("Already connected, ignoring send");
    return;
  }
  buffer[0] = HELLO_REPLY;
  size_t index = 2;

  std::cout <<address_info().dataSize() <<std::endl;
  size_t node_data_len = connections.size()*(address_info().dataSize());

  if (node_data_len > BUFFER_SIZE - 2) {
    error("HELLO_REPLY message too long, ignoring send."); 
  }

  buffer[1] = connections.size();
  std::cout <<"connection size: " << connections.size() <<std::endl;

  for(size_t i = 0; i < connections.size(); i++) {
    std::string serialized = connections[i].serialize();
    std::copy(serialized.begin(), serialized.end(), buffer + index);
    index += serialized.size();
  }

  size_t length = node_data_len + 2;
  std::cout << address_info(sender) <<" len: "<< length <<std::endl;
  ssize_t sent_length = sendto(socket_fd, buffer, length, 0, (struct sockaddr*)&sender, sender_len);
  if (sent_length < 0) {
    syserr("sendto HELLO_REPLY");
  }
  else if ((size_t) sent_length != length) {
    fatal("incomplete sending");
  }
  ind = addConnection(sender, CONFIRMED); //TODO check this.
  if(ind >= 0) {
    error("Already connected, ignoring send");
    return;
  }
}

void NetworkManager::handleHelloReply(const sockaddr_in& sender, ssize_t len) {
  // Parse peer list and send CONNECT messages.
  // Here the sender is the peer it was checked already, it is not connected;
  std::vector<address_info> peers;

  printBuffer(len);

  if (!_parseHelloReply(len, peers)){
    error("Invalid HELLO_REPLY buffer, ignoring send.");
    return;
  }

  for (const auto& peer : peers) {
    std::cout << "Found peer from HELLO_REPLY: " << peer << std::endl;
    addConnection(peer, UNCONFIRMED);
    sendMessage(peer, CONNECT); // Send CONNECT to each known peer.
  }

  addConnection(sender, CONFIRMED);
}

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
  // TODO here set recieve time.
#ifdef DEBUG
  std::cout << "[Node] Program uptime: " << GlobalClock::now() << " ms\n"; 
#endif // DEBUG 
  if (len < 0) {
    if (errno == EAGAIN || errno == EWOULDBLOCK) {
      // no data arrived in timeout period
      return;
    }
    // error("recvfrom");
#ifdef DEBUG
    std::cout <<"recvfrom error or timeout" << std::endl;
#endif // DEBUG
    error("recvfrom");
    return;
  } else if (len == 0) {
    error("incomplete datagram");
    return;
  }

  // Parse the message type
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
    // Quick check if its a valid peer address and is not connected.
    if (data.getPeerAddress() != sender_addr || _getFlag(sender_addr) != UNKNOWN) {
      error("MSG"); // TODO add the hex from message.
    } else {
      handleHelloReply(sender_addr, len);
    }
    break;

    case CONNECT:
#ifdef DEBUG
    std::cout << "Received CONNECT from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif 
    // Respond with ACK_CONNECT
    // TODO if i understand corrcelty connect is always ok.
    if (_findSockaddr(sender_addr) == UNKNOWN) {
      addConnection(sender_addr, CONFIRMED);
      sendMessage(sender_addr, ACK_CONNECT);
    } else {
      error("MSG");
    }
    break;

    case ACK_CONNECT:
#ifdef DEBUG
    std::cout << "Received ACK_CONNECT from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif 
    if (_getFlag(sender_addr) == UNCONFIRMED) { 
      addConnection(sender_addr, CONFIRMED);
    } else {
      std::cout <<int(_getFlag(sender_addr)) << std::endl;
      error("MSG UNCONFIRMED");
    }
    break;
    case SYNC_START:
#ifdef DEBUG
    std::cout << "Received SYNC_START from "
      << inet_ntoa(sender_addr.sin_addr) << ":"
      << ntohs(sender_addr.sin_port) << "\n";
#endif
    handleSyncStart(sender_addr, len, recv_time);
    // NEED to handle who sent the sync_request.
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
      error("Unknown message.");
      break;
  }
}

int NetworkManager::addConnection(const sockaddr_in& sender, short flag) {
  address_info addr_short = address_info(sender, flag);
  return addConnection(addr_short, flag);
}
// Functions to manage saved connections.
// IMPORTANT connections are confiremed if and only if they received a message from that adress.
int NetworkManager::addConnection(const address_info& addr_short, short flag) {

  int index = _findSockaddr(addr_short); 
  if (index < 0) {
    connections.push_back(addr_short);
  } else if (flag != connections[index].flags) {
    connections[index].flags = flag;
  } else {
    error("Duplicate connection request");
    index = -2; //TODO may be wierd
  }
  return index;
}


int NetworkManager::_findSockaddr(const address_info& peer_addr) {
  for (size_t i = 0; i < connections.size(); ++i) {
    if (connections[i] == peer_addr) {
      return static_cast<int>(i);
    }
  }
  return -1;
}

short NetworkManager::_getFlag(const sockaddr_in& connection) {
  return _getFlag(address_info(connection));
}

short NetworkManager::_getFlag(const address_info& connection) {
  int index = _findSockaddr(connection);
  if (index == -1) {
    return -1;
  }
  return connections[index].flags;
}

// Synchronization functions.

void NetworkManager::syncToAll() {
  for (auto& addr : connections) {
    sendSyncWithData(addr, SYNC_START, false);
    addr.flags = SYNCHRONIZING;
    // addr.event_time = GlobalClock::now(); TODO bin
  }
}

inline uint64_t htonll(uint64_t value) {
#if __BYTE_ORDER == __LITTLE_ENDIAN
    return (static_cast<uint64_t>(htonl(value & 0xFFFFFFFF)) << 32) |
            htonl(value >> 32);
#else
    return value;
#endif
}

inline uint64_t ntohll(uint64_t value) {
#if __BYTE_ORDER == __LITTLE_ENDIAN
    return (static_cast<uint64_t>(ntohl(value & 0xFFFFFFFF)) << 32) |
            ntohl(value >> 32);
#else
    return value;
#endif
}

void NetworkManager::sendSyncWithData(const address_info& peer_addr, MessageType type, bool offset_flag) {
  if (type != TIME && (type <= ACK_CONNECT || DELAY_RESPONSE < type || DELAY_REQUEST == type)) {
    fatal("Wrong message type!!");
  }
#ifdef DEBUG
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
  if (offset_flag) {
    timestamp -= GlobalClock::getOffset();
  }
  timestamp = htonll(timestamp);
  std::memcpy(buffer + 2, &timestamp, 8); 
  sockaddr_in send = peer_addr.toSockaddrIn(); 
  ssize_t sent_length = sendto(socket_fd, buffer, 10, 0, (struct sockaddr*)&send, sizeof(send));
  if (sent_length < 0) {
    syserr("sendto");
  }
  else if ((size_t) sent_length != 10) {
    fatal("incomplete sending");
  }
}

bool NetworkManager::validSyncRequest(const sockaddr_in& sender_addr, uint8_t synchronized) {
  std::cout << GREEN << GlobalClock::now() - synced_time << RESET << std::endl;
  if ((state == SYNCHRONIZED && data.getSyncAddr() == sender_addr) &&
    ((data.getSyncLevel() <= synchronized) || (GlobalClock::now() - synced_time >= 4*FIVE_SECONDS))) {
    data.setSyncLevel(255);
    state = UNSYNCHRONIZED;
    return false;
  }
  return true;
}
// Child node.
void NetworkManager::handleSyncStart(const sockaddr_in& sender_addr, ssize_t len, uint64_t recv_time) {
  short flag = _getFlag(sender_addr);
  if (len != 10 || flag == UNCONFIRMED || flag == UNKNOWN) {
    error("MSG");
    return;
  }
  uint8_t synchronized = buffer[1];
  if (synchronized >= 254 || state == LEADING) {
    return;
  }
  uint64_t time;
  memcpy(&time, buffer + 2, 8);
  time = ntohll(time);

  if (!validSyncRequest(sender_addr, synchronized)) {
    error("MSG"); // TODO
    return;
  }

  if ((state == SYNCHRONIZED && data.getSyncAddr() == sender_addr && synchronized < data.getSyncLevel()) ||
    (state != SYNCHRONIZED && synchronized + 2 <= data.getSyncLevel()) ) {
    GlobalClock::saveT(1, time);
    GlobalClock::saveT(2, recv_time);
    GlobalClock::saveT(3);
    data.setSyncAddr(address_info(sender_addr), synchronized);
    sendMessage(sender_addr, DELAY_REQUEST);
  } 

}

bool NetworkManager::checkTime(const sockaddr_in& sender_addr) {
  int index = _findSockaddr(address_info(sender_addr));
  std::cout <<GREEN <<"check time: " <<connections[index].sync_time <<" " << FIVE_SECONDS << " " << GlobalClock::now()<<'\n' << RESET;

  return (GlobalClock::now() - connections[index].sync_time) <= 2 * FIVE_SECONDS;
}

// Father node.
void NetworkManager::handleDelayRequest(const sockaddr_in& sender_addr) {
  short flag = _getFlag(sender_addr);
  if (flag != SYNCHRONIZING || !checkTime(sender_addr)) {
    std::cout << flag <<" time check: "<<checkTime(sender_addr) << std::endl;
    error("MSG delay Request");
    return;
  }
  addConnection(sender_addr, SYNCED); 
  sendSyncWithData(address_info(sender_addr), DELAY_RESPONSE, state == SYNCHRONIZED);
}

//Child node.
void NetworkManager::handleDelayResponse(const sockaddr_in& sender_addr, ssize_t len, uint64_t recv_time) {
  if (len != 10) {
    error("MSG len");
    return;
  }
  uint8_t synchronized = buffer[1];
  if (data.getSyncLevelSyncWith() != synchronized) {
    error("MSG sync");
    return;
  }
  if (!validSyncRequest(sender_addr, synchronized)) {
    error("MSG valid delay");
    return;
  }
  std::cout <<"state: "<< LEADING <<"recv: "<<address_info(sender_addr) <<
    "should be: " << data.getSyncAddr() <<std::endl;
  if ((data.getSyncAddr() == sender_addr) && (state != LEADING)) {
    GlobalClock::saveT(4, recv_time);
    GlobalClock::calcOffset();
    synced_time = GlobalClock::now();
    state = SYNCHRONIZED;
    data.setSyncLevel(synchronized + 1);
  } else {
    error("MSG diff sender or leader");
    return;
  }
}

void NetworkManager::handleLeader(ssize_t len) {
  if (len != 2) {
    error("MSG too short");
    return;
  }
  uint8_t flag = buffer[1];
  std::cout << GREEN << int(flag) <<" : "<<int(state) << RESET <<std::endl;
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
  error("MSG no such case");
}

void NetworkManager::handleSyncSending() {
  #ifdef DEBUG
  #endif //DEBUG
  std::cout<< BLUE <<"handling sending" << RESET <<std::endl;
  if (state == STOPLEADING) {
    bool finished = true;
    for (auto connection : connections) {
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
    std::cout << YELLOW << "High sync level" << RESET <<std::endl;
    return;
  }

  for (auto& connection : connections) {
    std::cout << connection <<std::endl;
    if (GlobalClock::now() - connection.sync_time > FIVE_SECONDS) {
      std::cout <<"send sync" <<connection.sync_time <<" " << FIVE_SECONDS << " " << GlobalClock::now()<<'\n';
      connection.sync_time = GlobalClock::now();
      std::cout <<"send sync 2" <<connection.sync_time <<" " << FIVE_SECONDS << " " << GlobalClock::now()<<'\n';
      if (state == SYNCHRONIZED && connection != addr) {
        sendSyncWithData(connection, SYNC_START, true);
      } else {
        sendSyncWithData(connection, SYNC_START, false);
      }
      connection.flags = SYNCHRONIZING;
    } 
  } 
}
