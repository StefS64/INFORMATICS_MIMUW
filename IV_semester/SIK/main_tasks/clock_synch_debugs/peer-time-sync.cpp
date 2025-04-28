#include<iostream>
#include<string>  
#include<unistd.h>
#include<time.h>

#include "node_config.hpp"
#include "network_manager.hpp"
#include "global_clock.hpp"

int main(int argc, char* argv[]) {
  GlobalClock::initialize();
  NodeConfig node;
  
  if(!node.parseArgs(argc, argv)) {
    return EXIT_FAILURE;
  }

  node.initSocket();

  NetworkManager net = NetworkManager(node.getSocketFd(), node);
  net.runNode();
}
