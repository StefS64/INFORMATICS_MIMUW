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
  short flags = 0;  // Flags for communication 0 means computer is not sure if it exists. 
  address_info() : ip(INADDR_ANY), port(0) {}  // Dummy address.
  address_info(in_addr_t ip, uint16_t port) : ip(ip), port(port) {}

  address_info(const char* ip_str, const char* port_str) { // Most likely won't be used. DEBUG TODO
    if (inet_pton(AF_INET, ip_str, &ip) != 1) {
      fatal("Invalid IP address: %s", ip_str);
    }
    port = read_port(port_str);
  }
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

  bool isValid() const {//helper may be useless TODO
    return ip != 0 && port != 0;
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
  
  std::string serialize() {
    std::string data;
    size_t size = sizeof(ip);
    data.push_back(static_cast<char>(size));
    const char* ip_bytes = reinterpret_cast<const char*>(&ip);
    data.append(ip_bytes, sizeof(ip));

    const char* port_bytes = reinterpret_cast<const char*>(&port);
    data.append(port_bytes, sizeof(port));
    std::cout <<"string data: " << data <<std::endl;
    std::cout <<"port: " << port <<std::endl;  
    printBinary(data);
    return data;
  }

  const size_t dataSize() {
    return sizeof(ip) + sizeof(port) + 1;// +1 for length byte
  }
// TODO debug functio// TODO debug functionn
void printBinary(const std::string& data) {
    std::cout << "Binary string: ";
    for (unsigned char c : data) {
        std::bitset<8> bits(c);
        std::cout << bits << ' ';
    }
    std::cout << std::endl;
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
  int getSocketFd();

private:
  uint8_t sync_level;
  bool peer_present;
  address_info peer_addr;
  sockaddr_in bind_addr;
  int socket_fd;
};

#endif
