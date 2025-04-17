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

class NetworkManager {
public:
  NetworkManager(int socket_fd);
  void sendHello(const sockaddr_in& peer_addr);
  void sendHelloReply(const sockaddr_in& peer_addr, const std::vector<sockaddr_in>& known_peers);
  void sendConnect(const sockaddr_in& peer_addr);
  void sendAckConnect(const sockaddr_in& peer_addr);
  void handleIncomingMessages(NodeConfig& node); 

private:
  int socket_fd;
  std::vector<sockaddr_in> peer_list;

  void addPeer(const sockaddr_in& peer_addr);
  void printPeerList() const;
};

#endif
