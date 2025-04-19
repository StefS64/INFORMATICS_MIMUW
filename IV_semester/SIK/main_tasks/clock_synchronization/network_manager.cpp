#include "network_manager.hpp"

#define BUFFER_SIZE 65507 // Maximal size of package for my MTU

// #define DEBUG
#ifdef DEBUG
#include <iomanip>  // for std::setw and std::setfill
#include <bitset>
#endif
static char buffer[BUFFER_SIZE];

NetworkManager::NetworkManager(int socket_fd) : socket_fd(socket_fd) {
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

// Function for sending single 1 byte Message. Not suitable for HELLO_REPLY
void NetworkManager::sendMessage(const address_info& peer_addr, MessageType type) {
  if (type == HELLO_REPLY) {
    fatal("Wrong message type HELLO_REPLY");
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
    default:           typeStr = "UNKNOWN"; break;
  }
  std::cout << "Sent " << typeStr << " to " << peer_addr << "\n";
#endif
}

// Parse helper for HELLO_REPLY only more complicated reply function 
bool _parseHelloReply(size_t len, std::vector<address_info>& peers) {
  if (len < 2) return false;
  uint8_t count = static_cast<uint8_t>(buffer[1]);
  size_t index   = 2;
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
    sendMessage(peer, CONNECT); // Send CONNECT to each known peer.
  }

  int ind = addConnection(sender, CONFIRMED);

  if(ind >= 0) {
    error("Already connected, ignoring send.");
    return;
  }
}

void NetworkManager::handleIncomingMessages(NodeConfig& node) {
  memset(buffer, 0, sizeof(buffer));
  struct sockaddr_in sender_addr;
  socklen_t sender_len = sizeof(sender_addr);
  int flags = 0;

  while (true) {
    ssize_t len = recvfrom(socket_fd, buffer, BUFFER_SIZE, flags,
                           (struct sockaddr*)&sender_addr, &sender_len);
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
      handleHelloReply(sender_addr, len);
      break;

      case CONNECT:
#ifdef DEBUG
      std::cout << "Received CONNECT from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif 
      // Respond with ACK_CONNECT
      addConnection(sender_addr, CONFIRMED);
      sendMessage(sender_addr, ACK_CONNECT);

      break;

      case ACK_CONNECT:
#ifdef DEBUG
      std::cout << "Received ACK_CONNECT from "
        << inet_ntoa(sender_addr.sin_addr) << ":"
        << ntohs(sender_addr.sin_port) << "\n";
#endif 
      addConnection(sender_addr, CONFIRMED);
      break;

      default:
        std::cerr << "Unknown message type received.\n";
        break;
    }
  }
}

// Functions to manage saved connections.
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


