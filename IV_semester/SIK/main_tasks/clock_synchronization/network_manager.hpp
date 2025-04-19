#ifndef NETWORK_MANAGER_HPP
#define NETWORK_MANAGER_HPP

#include <vector>
#include <string>
#include <netinet/in.h>
#include <cstdint>
#include <iostream>
#include <cstring>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include "node_config.hpp"
extern "C" {
#include "err.h"
}
enum ConnectionState {
  UNCONFIRMED   = 0, 
  CONFIRMED     = 1
}; 
enum MessageType : uint8_t {
  HELLO           = 1,
  HELLO_REPLY     = 2,
  CONNECT         = 3,
  ACK_CONNECT     = 4,
  SYNC_STRT       = 11,
  DELAY_REQUEST   = 12,
  DELAY_RESPONSE  = 13,
  LEADER          = 21,
  GET_TIME        = 31,
  TIME            = 32
};

class NetworkManager {
public:
  NetworkManager(int socket_fd);
  void sendHello(const address_info& peer_addr);
  void handleIncomingMessages(NodeConfig& node);
private:
  int socket_fd;
  std::vector<address_info> connections;

  int addConnection(const sockaddr_in& peeaddr, const short flags = 0);
  void printConnectionList() const;
  int _findSockaddr(const address_info& peer_addr);
  void handleHello(const sockaddr_in& sender,socklen_t sender_len);
  void handleHelloReply(const sockaddr_in& sender, ssize_t len);
  void sendMessage(const address_info& peer_addr, MessageType type);
};

#endif
