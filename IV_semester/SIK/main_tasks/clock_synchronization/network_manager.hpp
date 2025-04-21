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
#include "global_clock.hpp"
extern "C" {
#include "err.h"
}
enum ConnectionState : short {
  UNKNOWN       = -1,
  UNCONFIRMED   = 0, 
  CONFIRMED     = 1,
  SYNCHRONIZING = 2,
  SYNCED        = 3
};

enum MessageType : uint8_t {
  HELLO           = 1,
  HELLO_REPLY     = 2,
  CONNECT         = 3,
  ACK_CONNECT     = 4,
  SYNC_START      = 11,
  DELAY_REQUEST   = 12,
  DELAY_RESPONSE  = 13,
  LEADER          = 21,
  GET_TIME        = 31,
  TIME            = 32
};
enum SynchronizationState : uint8_t {
  LEADING           = 1,
  SYNCHRONIZED      = 2,
  UNSYNCHRONIZED    = 3,
  STOPLEADING       = 4
};    

constexpr uint64_t FIVE_SECONDS = 5000;

class NetworkManager {
public:
  NetworkManager(int socket_fd, NodeConfig& node);
  void runNode();
private:
  int socket_fd;
  NodeConfig& data;
  std::vector<address_info> connections;
  SynchronizationState state;
  uint64_t synced_time;

  void handleIncomingMessage();
  void handleSyncSending();
  bool validSyncRequest(const sockaddr_in& sender_addr, uint8_t synchronized);
  int addConnection(const sockaddr_in& sender, const short flags = 0);
  int addConnection(const address_info& peeaddr, const short flags = 0);
  void printConnectionList() const;
  int _findSockaddr(const address_info& peer_addr);
  void handleHello(const sockaddr_in& sender,socklen_t sender_len);
  void handleHelloReply(const sockaddr_in& sender, ssize_t len);
  void handleSyncStart(const sockaddr_in& sender_addr, ssize_t len, uint64_t recv_time);
  void sendMessage(const address_info& peer_addr, MessageType type);
  void sendSyncWithData(const address_info& peer_addr, MessageType type, bool offsetPresent);
  short _getFlag(const sockaddr_in& connection);
  short _getFlag(const address_info& connection);
  void handleDelayRequest(const sockaddr_in& sender_addr);
  void handleDelayResponse(const sockaddr_in& sender_addr, ssize_t len, uint64_t recv_time);
  bool checkTime(const sockaddr_in& sender_addr);
  void handleLeader(ssize_t len);
  void syncToAll();
};

#endif
