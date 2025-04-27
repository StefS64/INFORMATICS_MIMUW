#include <iostream>
#include <string>
#include <cstring>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>

std::string getLocalAddressForPeer(const std::string& peer_ip, int peer_port) {
    int sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) {
        perror("socket");
        return "";
    }

    struct sockaddr_in peer_addr;
    memset(&peer_addr, 0, sizeof(peer_addr));
    peer_addr.sin_family = AF_INET;
    peer_addr.sin_port = htons(peer_port);
    inet_pton(AF_INET, peer_ip.c_str(), &peer_addr.sin_addr);

    // Connect to peer (no data sent yet)
    if (connect(sock, (struct sockaddr*)&peer_addr, sizeof(peer_addr)) < 0) {
        perror("connect");
        close(sock);
        return "";
    }

    struct sockaddr_in local_addr;
    socklen_t addr_len = sizeof(local_addr);
    if (getsockname(sock, (struct sockaddr*)&local_addr, &addr_len) == 0) {
        std::string local_ip = inet_ntoa(local_addr.sin_addr);
        close(sock);
        return local_ip;
    } else {
        perror("getsockname");
        close(sock);
        return "";
    }
}

int main() {
    std::string peer_ip = "192.168.1.50"; // example sender
    int peer_port = 12345; // example sender port

    std::string local_ip = getLocalAddressForPeer(peer_ip, peer_port);
    std::cout << "Local IP used to reach " << peer_ip << " is: " << local_ip << std::endl;

    return 0;
}

