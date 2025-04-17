#include<iostream>
#include<string>
#include<unistd.h>
extern "C" {
#include "common.h"
}
#include "node_config.hpp"
#include "network_manager.hpp"
#define DEBUG

void printUsage(const char* programName) {
  std::cerr << "Usage: " << programName 
    << " [-b <bind_address>] [-p <port>] [-a <peer_address> -r <peer_port>]" 
    << std::endl;
}

int main(int argc, char* argv[]) {
  NodeConfig node;
  
  if(!node.parseArgs(argc, argv)) {
    std::cerr << "Error parsing arguments.\n";
    node.printUsage(argv[0]);
    return EXIT_FAILURE;
  }
  #ifdef DEBUG
  std::cout << "Configuration parsed successfully.\n";
  std::cout << "Bind Address: " << inet_ntoa(node.getBindAddress().sin_addr) << "\n";
  std::cout << "Port: " << ntohs(node.getBindAddress().sin_port) << "\n";
  if (node.isPeerPresent()) {
    std::cout << "Peer Address: " << inet_ntoa(node.getPeerAddress().sin_addr) << "\n";
    std::cout << "Peer Port: " << ntohs(node.getPeerAddress().sin_port) << "\n";
  }
  #endif
  NetworkManager net = NetworkManager(node.getSocketFd());
  node.initSocket();

  net.sendHello(node.getPeerAddress());
  while(true) {
    net.handleIncomingMessages(node);
  }
}
