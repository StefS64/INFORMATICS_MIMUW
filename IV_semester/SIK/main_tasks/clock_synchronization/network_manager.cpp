#include "network_manager.hpp"

#define BUFFER_SIZE 65507
#define HELLO 1
#define HELLO_REPLY 2
#define CONNECT 3
#define ACK_CONNECT 4


#define DEBUG

static char buffer[BUFFER_SIZE];

NetworkManager::NetworkManager(int socket_fd) : socket_fd(socket_fd) {}

void NetworkManager::sendHello(const sockaddr_in& peer_addr) {
  memset(buffer, 0, sizeof(buffer));
  buffer[0] = HELLO;
  sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&peer_addr, sizeof(peer_addr));
  std::cout << "Sent HELLO to " << inet_ntoa(peer_addr.sin_addr) << ":"
    << ntohs(peer_addr.sin_port) << "\n";
}

void NetworkManager::handleHello(const sockaddr_in& sender) {
  addConnection(sender);
  // TODO for now even if sender is already connected it still add's it.
  buffer[0] = HELLO_REPLY;
  sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&sender_addr, sender_len);
}

void NetworkManager::handleHelloReply(const sockaddr_in& sender) {
  addConnection(sender);
   
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
      perror("recvfrom");
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
        handleHello(sender_addr);
        break;

      case HELLO_REPLY:
        #ifdef DEBUG
        std::cout << "Received HELLO_REPLY from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        #endif 
        handleHelloReply(sender_addr);
        // Parse peer list and send CONNECT messages
        break;

      case CONNECT:
        #ifdef DEBUG
        std::cout << "Received CONNECT from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        // Respond with ACK_CONNECT
        #endif 
        handleHello(sender_addr);
        buffer[0] = ACK_CONNECT;
        sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&sender_addr, sender_len);
        break;

      case ACK_CONNECT:
        #ifdef DEBUG
        std::cout << "Received ACK_CONNECT from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        #endif 
        handleHello(sender_addr);
        break;

      default:
        std::cerr << "Unknown message type received.\n";
        break;
    }
  }
}

void addConnection(const sockaddr_in& sender) {
  if (find_sockaddr(sender)) {
    connections.push_back(sender);
  } else {
    // TODO May change later this place is a bit erronous.
    std::cerr << "Duplicate connection request" <<std::endl;
  }
}


bool compare_sockaddr(const sockaddr_in& a, const sockaddr_in& b) {
  return a.sin_family == b.sin_family &&
  a.sin_port == b.sin_port &&
  a.sin_addr.s_addr == b.sin_addr.s_addr;
}

int find_sockaddr(const std::vector<sockaddr_in>& list, const sockaddr_in& peer_addr) {
    for (size_t i = 0; i < list.size(); ++i) {
        if (compare_sockaddr(list[i], peer_addr)) {
            return static_cast<int>(i);
        }
    }
    return -1;
}

