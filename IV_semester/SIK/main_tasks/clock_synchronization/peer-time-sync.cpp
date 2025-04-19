#include<iostream>
#include<string>  
#include<unistd.h>
#include<time.h>
extern "C" {
#include "common.h"
}
#include "node_config.hpp"
#include "network_manager.hpp"
#include "global_clock.hpp"
#define DEBUG

void printUsage(const char* programName) {
  std::cerr << "Usage: " << programName 
    << " [-b <bind_address>] [-p <port>] [-a <peer_address> -r <peer_port>]" 
    << std::endl;
}

int main(int argc, char* argv[]) {
  GlobalClock::initialize();
  NodeConfig node;
  
  if(!node.parseArgs(argc, argv)) {
    node.printUsage(argv[0]);
    return EXIT_FAILURE;
  }

  node.initSocket();
#ifdef DEBUG 
  std::cout << "Configuration parsed successfully.\n";
  std::cout << "Bind Address: " << inet_ntoa(node.getBindAddressIn().sin_addr) << "\n";
  std::cout << "Port: " << ntohs(node.getBindAddressIn().sin_port) << "\n";
  if (node.isPeerPresent()) {
    std::cout << node.getPeerAddress() << "\n";
  }
#endif

  NetworkManager net = NetworkManager(node.getSocketFd(), node);
  if (node.isPeerPresent()) {
    net.sendHello(node.getPeerAddress());
  }
  net.handleIncomingMessages();
}
