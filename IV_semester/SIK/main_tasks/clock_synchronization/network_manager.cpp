#include "network_manager.hpp"

#define BUFFER_SIZE 1024
#define HELLO 1
#define HELLO_REPLY 2
#define CONNECT 3
#define ACK_CONNECT 4
NetworkManager::NetworkManager(int socket_fd) {
  this->socket_fd = socket_fd;
}

void NetworkManager::sendHello(const sockaddr_in& peer_addr) {
  char buffer[BUFFER_SIZE];
  buffer[0] = HELLO;
  sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&peer_addr, sizeof(peer_addr));
  std::cout << "Sent HELLO to " << inet_ntoa(peer_addr.sin_addr) << ":"
    << ntohs(peer_addr.sin_port) << "\n";
}
void  NetworkManager::sendHelloReply(const sockaddr_in& peer_addr, const std::vector<sockaddr_in>& known_peers){}
void NetworkManager::sendConnect(const sockaddr_in& peer_addr){}
void NetworkManager::sendAckConnect(const sockaddr_in& peer_addr){}

void NetworkManager::handleIncomingMessages(NodeConfig& node) {
  static char buffer[BUFFER_SIZE];
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
        std::cout << "Received HELLO from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        // Respond with HELLO_REPLY
        buffer[0] = HELLO_REPLY;
        sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&sender_addr, sender_len);
        break;

      case HELLO_REPLY:
        std::cout << "Received HELLO_REPLY from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        // Parse peer list and send CONNECT messages
        break;

      case CONNECT:
        std::cout << "Received CONNECT from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        // Respond with ACK_CONNECT
        buffer[0] = ACK_CONNECT;
        sendto(socket_fd, buffer, 1, 0, (struct sockaddr*)&sender_addr, sender_len);
        break;

      case ACK_CONNECT:
        std::cout << "Received ACK_CONNECT from "
          << inet_ntoa(sender_addr.sin_addr) << ":"
          << ntohs(sender_addr.sin_port) << "\n";
        // Add peer to the list of connected peers
        break;

      default:
        std::cerr << "Unknown message type received.\n";
        break;
    }
  }
}
