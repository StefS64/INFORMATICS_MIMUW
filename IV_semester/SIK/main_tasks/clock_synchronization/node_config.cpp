#include "node_config.hpp"

NodeConfig::NodeConfig() : sync_level(255), peer_present(false), socket_fd(-1) {
  memset(&bind_addr, 0, sizeof(bind_addr));
  bind_addr.sin_family = AF_INET;
  bind_addr.sin_addr.s_addr = htonl(INADDR_ANY);
  bind_addr.sin_port = 0;

  memset(&peer_addr, 0, sizeof(peer_addr));
  peer_addr.sin_family = AF_INET;
  peer_addr.sin_port = 0;
}

NodeConfig::~NodeConfig() {
  if (socket_fd != -1) {
    close(socket_fd);
    std::cout << "Socket closed: " << socket_fd << std::endl;
  }
}

bool NodeConfig::parseArgs(int argc, char* argv[]) {
  bool a_flag = false, r_flag = false;
  int opt;
  while ((opt = getopt(argc, argv, "b:p:a:r:")) != -1) {
    switch (opt) {
      case 'b':
        std::cout << "Option bind_address\n";
        if (inet_pton(AF_INET, optarg, &bind_addr.sin_addr) != 1) {
          std::cerr << "ERROR: Invalid bind address: " << optarg << "\n";
          return false;
        }
        break;
      case 'p':
        bind_addr.sin_port = htons(read_port(optarg));
        std::cout << "Option port with value: " << bind_addr.sin_port << "\n";
        break;
      case 'a':
        std::cout << "Option\n";       
        if (inet_pton(AF_INET, optarg, &peer_addr.sin_addr) != 1) {
          std::cerr << "ERROR: Invalid peer address: " << optarg << "\n";
          return false;
        }
        a_flag = true;
        peer_present = true;
        break;
      case 'r':
        std::cout << "Option c\n";
        peer_addr.sin_port = htons(read_port(optarg));
        r_flag = true;
        peer_present = true;
        break;
      case '?':
        printUsage(argv[0]);
        break;
    }
  }
  
  //Validation of args.
  if (peer_present && (!a_flag || !r_flag)) {
    std::cerr << "ERROR: Both '-a' and '-r' must be provided together.\n";
    return false;
  }

  if (optind < argc) {
    std::cerr << "ERROR: Unexpected arguments provided: ";
    for (int i = optind; i < argc; ++i) {
      std::cerr << argv[i] << " ";
    }
    std::cerr << "\n";
    return false;
  }

  return true;
}

void NodeConfig::printUsage(const char* programName) const {
  std::cerr << "Usage: " << programName
            << " [-b <bind_address>] [-p <port>] [-a <peer_address> -r <peer_port>]"
            << std::endl;
}

void NodeConfig::initSocket() {
  int socket_fd = socket(AF_INET, SOCK_DGRAM, 0);
  if (socket_fd < 0) {
    syserr("cannot create a socket");
  }

  // Bind the socket to a concrete address.
  if (bind(socket_fd, (struct sockaddr *) &bind_addr, (socklen_t) sizeof(bind_addr)) < 0) {
    syserr("bind");
  }
} 

sockaddr_in NodeConfig::getBindAddress() const {
  return bind_addr;
}

sockaddr_in NodeConfig::getPeerAddress() const {
  return peer_addr;
}

bool NodeConfig::isPeerPresent() const {
  return peer_present;
}

uint8_t NodeConfig::getSyncLevel() const {
  return sync_level;
}

void NodeConfig::setSyncLevel(uint8_t level) {
  sync_level = level;
}

int NodeConfig::getSocketFd() {
  return socket_fd;
}
