#include "network_manager.hpp"

#define BUFFER_SIZE 65507 // Maximal size of package for my MTU

#define DEBUG
#ifdef DEBUG
#include <iomanip>  // for std::setw and std::setfill
#include <bitset>
#endif
static char buffer[BUFFER_SIZE];

NetworkManager::NetworkManager(int socket_fd, const NodeConfig& data) : socket_fd(socket_fd), data(data), state(UNSYNCHRONIZED) {
  #ifdef DEBUG
  std::cout << "NetworkManager up: " << GlobalClock::now() << " ms\n";
  #endif // DEBUG
  // Dummy test variables.
  // connections = {
  //   address_info("192.34.23.10", "1234"),
  //   address_info("192.34.23.10", "1234"),
  //   address_info("192.34.23.10", "1234"),
  // };
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
*  Send hello is special as we allow it to be used in the interface of this class.
*
*/
void NetworkManager::sendHello(const address_info& peer_addr) {
  sendMessage(peer_addr, HELLO);
}

// Function for sending single 1 byte Message. Not suitable for HELLO_REPLY, SYNC_START, DELAY_RESPONSE, TIME.
void NetworkManager::sendMessage(const address_info& peer_addr, MessageType type) {
  if (type == HELLO_REPLY || type == SYNC_START || type == DELAY_RESPONSE || type == TIME) {
    fatal("Wrong message type for sendMessage");
  }
  memset(buffer, 0, sizeof(buffer));
  buffer[0] = type;
  sockaddr_in send = peer_addr.toSockaddrIn(); 
  sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&send, sizeof(send));

#ifdef DEBUG
  std::string typeStr;
  switch (type) {
    case HELLO:        typeStr = "HELLO"; break;
    case CONNECT:      typeStr = "CONNECT"; break;
    case ACK_CONNECT:  typeStr = "ACK_CONNECT"; break;
    // case HELLO:        typeStr = "HELLO"; break;
    // case CONNECT:      typeStr = "CONNECT"; break;
    // case ACK_CONNECT:  typeStr = "ACK_CONNECT"; break;
    default:           typeStr = "UNKNOWN"; break;
  }
  std::cout << "Sent " << typeStr << " to " << peer_addr << "\n";
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
  int ind = _findSockaddr(sender);
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
  std::cout <<connections.size() <<std::endl;

  for(size_t i = 0; i < connections.size(); i++) {
    std::string serialized = connections[i].serialize();
    std::copy(serialized.begin(), serialized.end(), buffer + index);
    index += serialized.size();
  } 

  size_t length = node_data_len + 2;
  sendto(socket_fd, buffer, length, 0, (struct sockaddr*)&sender, sender_len);
  ind = addConnection(sender, CONFIRMED);
  if(ind >= 0) {
    error("Already connected, ignoring send");
    return;
  }
}

void NetworkManager::handleHelloReply(const sockaddr_in& sender, ssize_t len) {
  // Parse peer list and send CONNECT messages.
  std::vector<address_info> peers;

  printBuffer(len);

  if (!_parseHelloReply(len, peers)){
    error("Invalid HELLO_REPLY buffer, ignoring send.");
    return;
  }

  for (const auto& peer : peers) {
    std::cout << "Found peer from HELLO_REPLY: " << peer << std::endl;
    addConnection(sender, UNCONFIRMED);
    sendMessage(peer, CONNECT); // Send CONNECT to each known peer.
  }

  int ind = addConnection(sender, CONFIRMED);

  if(ind >= 0) {
    error("Already connected, ignoring send.");
    return;
  }
}

void NetworkManager::handleIncomingMessages() {
  memset(buffer, 0, sizeof(buffer));
  struct sockaddr_in sender_addr;
  socklen_t sender_len = sizeof(sender_addr);
  int flags = 0;

  while (true) {
    ssize_t len = recvfrom(socket_fd, buffer, BUFFER_SIZE, flags,
                           (struct sockaddr*)&sender_addr, &sender_len);
    // TODO here set recieve time.
#ifdef DEBUG
    std::cout << "[Node] Program uptime: " << GlobalClock::now() << " ms\n"; 
#endif // DEBUG 
    if (len < 0) {
      error("recvfrom");
      continue;
    } else if (len == 0) {
      error("incomplete datagram");
      continue;
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
        if (data.getPeerAddress() != sender_addr || _getFlag(address_info(sender_addr)) != -1) {
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
        addConnection(sender_addr, CONFIRMED);
        sendMessage(sender_addr, ACK_CONNECT);
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
          error("MSG");
        }
        break;
      case SYNC_START:
#ifdef DEBUG
      std::cout << "Received SYNC_START from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif
        handleSyncStart(sender_addr, len);
        // NEED to handle who sent the sync_request.
        break;
      case DELAY_REQUEST:
#ifdef DEBUG
      std::cout << "Received DELAY_REQUEST from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif
      if (state == SYNCHRONIZED && data.getSyncAddr() == sender_addr) {// This case may be redundant TODO
        error("MSG");
      } else {
        sendSyncWithData(address_info(sender_addr), DELAY_RESPONSE);
      }
        break;
      case DELAY_RESPONSE:
#ifdef DEBUG
      std::cout << "Received DELAY_RESPONSE from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif
        // Doesn't return anything.
        break;
      case LEADER:
#ifdef DEBUG
      std::cout << "Received LEADER from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif
        if(len != 2) {
          error("MSG");// TODO print the errors
        }
        // Doesn't verify address.
        if (state == LEADING && buffer[1] == 255) {
          
        }
        break;
      case GET_TIME: // DONE
#ifdef DEBUG
      std::cout << "Received from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif
        sendSyncWithData(address_info(sender_addr), TIME);
        break;
      default:
        error("Unknown message.");
        break;
    }
  }
}

// Functions to manage saved connections.
// IMPORTANT connections are confiremed if and only if they received a message from that adress.
int NetworkManager::addConnection(const sockaddr_in& sender, short flag) {
  address_info addr_short = address_info(sender, flag);

  int index = _findSockaddr(addr_short); 
  if (index < 0) {
    connections.push_back(addr_short);
  } else if (flag != connections[index].flags) {
    connections[index].flags = flag;
  } else {
    error("Duplicate connection request");
    index = -1;
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

void NetworkManager::sendSyncWithData(const address_info& peer_addr, MessageType type) {
  if (type != TIME && (type <= ACK_CONNECT || DELAY_RESPONSE <= type || DELAY_REQUEST == type)) {
    fatal("Wrong message type!!");
  }
  memset(buffer, 0, sizeof(buffer));
  buffer[0] = type;
  buffer[1] = data.getSyncLevel();
  uint64_t timestamp = GlobalClock::now();
  if (type == TIME) {
    timestamp -= GlobalClock::getOffset();
  }
  std::memcpy(buffer + 2, &timestamp, 8);
  sockaddr_in send = peer_addr.toSockaddrIn(); 
  sendto(socket_fd, buffer, 10, 0, (struct sockaddr*)&send, sizeof(send));
}

void NetworkManager::handleSyncStart(const sockaddr_in& sender_addr, ssize_t len) {
  
}
