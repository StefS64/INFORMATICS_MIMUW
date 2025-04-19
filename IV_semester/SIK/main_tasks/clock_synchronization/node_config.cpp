#include "node_config.hpp"

NodeConfig::NodeConfig() : sync_level(255), peer_present(false), socket_fd(-1), sync_with(address_info()){
  memset(&bind_addr, 0, sizeof(bind_addr));
  bind_addr.sin_family = AF_INET;
  bind_addr.sin_addr.s_addr = htonl(INADDR_ANY);
  bind_addr.sin_port = 0;
  peer_addr = address_info(); 
}

NodeConfig::~NodeConfig() {
  if (socket_fd != -1) {
    close(socket_fd);
    std::cout << "Socket closed: " << socket_fd << std::endl;
  }
}

bool NodeConfig::parseArgs(int argc, char* argv[]) {
  bool a_flag = false, r_flag = false, b_flag = false, p_flag = false;
  int opt;
  std::string peer_address;
  uint16_t peer_port = 0; 

  while ((opt = getopt(argc, argv, "b:p:a:r:")) != -1) {
    switch (opt) {
      case 'b':
        if (b_flag) fatal("Duplicate '-b' (bind address) flag provided.");
        b_flag = true;

        std::cout << "Option bind_address\n";

        if (inet_pton(AF_INET, optarg, &bind_addr.sin_addr) != 1) {
          fatal("Invalid bind address: %s", optarg);
        }
        break;

      case 'p':
        if (p_flag) fatal("Duplicate '-p' (port) flag provided.");
        p_flag = true;
        bind_addr.sin_port = htons(read_port(optarg));

        std::cout << "Option port with value: " << ntohs(bind_addr.sin_port) << "\n";
        break;

      case 'a':
        if (a_flag) fatal("Duplicate '-a' (peer address) flag provided.");
        a_flag = true;
        peer_present = true;
        peer_address = optarg;

        std::cout << "Option peer_address\n";
        break;

      case 'r':
        if (r_flag) fatal("Duplicate '-r' (peer port) flag provided.");
        r_flag = true;
        peer_present = true;

        std::cout << "Option peer_port\n";
        peer_port = read_port(optarg);  // Here without htons because get_server_address does that for us.

        if (peer_port == 0) {
          fatal("Peer port number can't be zero");
        }
        break;

      case '?':
        printUsage(argv[0]);
        return false;
    }
  }

  // Validate peer address/port consistency
  if (peer_present && (!a_flag || !r_flag)) {
    fatal("Both '-a' and '-r' must be provided together.");
  }

  if (peer_present) {
    std::cout <<peer_port <<std::endl;
    peer_addr = address_info(get_server_address(peer_address.c_str(), peer_port));
  }

  // Checks if any extra arguments where given prints out the first one and exits.
  if (optind < argc) {
    fatal("Unexpected argument: %s", argv[optind]);
  }

  return true;
}


void NodeConfig::printUsage(const char* programName) const {
  std::cerr << "Usage: " << programName
    << " [-b <bind_address>] [-p <port>] [-a <peer_address> -r <peer_port>]"
    << std::endl;
}

void NodeConfig::initSocket() {
  socket_fd = socket(AF_INET, SOCK_DGRAM, 0);
  if (socket_fd < 0) {
    syserr("cannot create a socket");
  }

  // Bind the socket to a concrete address.
  if (bind(socket_fd, (struct sockaddr *) &bind_addr, (socklen_t) sizeof(bind_addr)) < 0) {
    syserr("bind");
  }

  // Find out what port the server is actually listening on.
  socklen_t lenght = (socklen_t) sizeof bind_addr;
  if (getsockname(socket_fd, (struct sockaddr *) &bind_addr, &lenght) < 0) {
    syserr("getsockname");
  }
  
  printf("listening on port %" PRIu16 "\n", ntohs(bind_addr.sin_port));
} 

sockaddr_in NodeConfig::getBindAddressIn() const {
  return bind_addr;
}

address_info NodeConfig::getPeerAddress() const {
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

address_info NodeConfig::getSyncAddr() const {
 return sync_with;  
}
int NodeConfig::getSocketFd() {
  return socket_fd;
}
