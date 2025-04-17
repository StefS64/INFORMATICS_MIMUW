#ifndef NODE_CONFIG_HPP
#define NODE_CONFIG_HPP
#include <cstdint>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <cstring>
#include <cstdlib>
#include <iostream>
#include <unistd.h>
extern "C" {
#include "common.h"
#include "err.h"    
}
class NodeConfig {
  // Default constructor defines correct values if no flags are used.
public:
  NodeConfig();
  ~NodeConfig();
  /* @brief
   * Parses command line args.
   *  -b bind_address   : Listening socket binding.
   *  -p port           : Specified port number for listening (0 means any port).
   *  -a peer_address   : IP or hostname of node to be connected.
   *  -r peer_port      : Port of the node to be connected with (in range 1-65535).
   *
   *  Error handling.
   *    If multiples of a flag are given the returns false.
   *    -a and -r must be given together.
   *
   *  @return true if ok false if wrong arguments
  */
  bool parseArgs(int argc, char* argv[]);
  sockaddr_in getBindAddress() const;
  sockaddr_in getPeerAddress() const;
  bool isPeerPresent() const;
  uint8_t getSyncLevel() const;
  void setSyncLevel(uint8_t level);
  void printUsage(const char* programName) const;
  void initSocket();
  int getSocketFd();

private:
  uint8_t sync_level;
  bool peer_present;
  sockaddr_in peer_addr;
  sockaddr_in bind_addr;
  int socket_fd;
  // May add possible freinds here 
};

#endif
