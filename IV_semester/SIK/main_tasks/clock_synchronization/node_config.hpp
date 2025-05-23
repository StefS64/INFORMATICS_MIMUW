#ifndef NODE_CONFIG_HPP
#define NODE_CONFIG_HPP
#include <cstdint>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <cstring>
#include <cstdlib>
#include <iostream>
#include <unistd.h>
#include <cinttypes>
#include <set>
#include <ifaddrs.h>
#include <tuple>

extern "C" {    
#include "common.h"
#include "err.h"    
}
// DEBUG includes
#include <bitset>
#include <iomanip>
struct address_info {
  in_addr_t ip;   // Stored in network byte order (like inet_pton output)
  uint16_t port;  // Stored in network byte order for flexibility
  mutable short flags = 0;  // Flags for communication 0 means computer is not sure if it exists.
  mutable uint64_t sync_time = 0; // Keeps track of time from last SYNC_START.
  //
  // Constructors.
  address_info() : ip(INADDR_ANY), port(0) {}  // Default constructor dummy addres.
  address_info(in_addr_t ip, uint16_t port) : ip(ip), port(port) {}
  address_info(const sockaddr_in& extract) : 
    ip(extract.sin_addr.s_addr),
    port(extract.sin_port) {} 
  address_info(const sockaddr_in& extract, const short flags) : 
    ip(extract.sin_addr.s_addr),
    port(extract.sin_port),
    flags(flags) {}

  // Quick conversion to sockaddr_in for sending.
  sockaddr_in toSockaddrIn() const {
    sockaddr_in addr{};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = ip;
    addr.sin_port = port;
    return addr;
  }

  friend std::ostream& operator<<(std::ostream& os, const address_info& addr) {
    struct in_addr ip_addr;
    ip_addr.s_addr = addr.ip;
    os << inet_ntoa(ip_addr) << ":" << ntohs(addr.port);
    return os;
  }

  bool operator==(const address_info& other) const {
    return ip == other.ip && port == other.port;
  }
  
  bool operator==(const sockaddr_in& other) const {
    return ip == other.sin_addr.s_addr && port == other.sin_port;
  }

  bool operator!=(const address_info& other) const {
    return !(*this == other);
  }

  bool operator!=(const sockaddr_in& other) const {
    return !(*this == other);
  }
  bool operator<(const address_info& other) const {
    return std::tie(ip, port) <
    std::tie(other.ip, other.port);
  }

  // Functions used to prep data for send.
  std::string serialize() const {
    std::string data;
    size_t size = sizeof(ip);
    data.push_back(static_cast<char>(size));
    const char* ip_bytes = reinterpret_cast<const char*>(&ip);
    data.append(ip_bytes, sizeof(ip));

    const char* port_bytes = reinterpret_cast<const char*>(&port);
    data.append(port_bytes, sizeof(port));
#ifdef DEBUG
    std::cout <<"string data: " << data <<std::endl;
    std::cout <<"port: " << port <<std::endl;  
#endif
    return data;
  }

  const size_t dataSize() {
    return sizeof(ip) + sizeof(port) + 1;// +1 for length byte
  }
};

/* Class created to initialize the node.
 * Parses arguments and then initializes the listening socket.
 *
 * @brief
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

class NodeConfig {
public:
  NodeConfig();
  ~NodeConfig();
  bool parseArgs(int argc, char* argv[]);
  sockaddr_in getBindAddressIn() const;
  address_info getPeerAddress() const;
  bool isPeerPresent() const;
  uint8_t getSyncLevel() const;
  void setSyncLevel(uint8_t level);
  void printUsage(const char* programName) const;
  void initSocket();
  int getSocketFd() const;
  address_info getSyncAddr() const;
  void setSyncAddr(const address_info sync_with, const uint8_t sync_level);
  uint8_t getSyncLevelSyncWith() const;
  uint64_t getOffset() const;
  void setOffset(uint64_t val);
  const std::set<address_info>& getLocalAddress() const;

private:
  uint8_t sync_level;
  bool peer_present;
  address_info peer_addr;// Begining sync address.
  sockaddr_in bind_addr;// Adress to bind with.
  int socket_fd;
  address_info sync_with;// Address of the synchronized node.
  uint8_t sync_level_of_synced_with; // Sync level of your peer.
  uint64_t offset;
  std::set<address_info> localAddress;// Ip of possible interfaces.
};

#endif
