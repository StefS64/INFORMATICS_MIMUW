#include <iostream>
#include <string>
#include <cstring>
#include <unistd.h>
#include <sys/types.h>
#include <ifaddrs.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main() {
    struct ifaddrs *interfaces = nullptr;
    struct ifaddrs *temp_addr = nullptr;

    if (getifaddrs(&interfaces) == 0) {
        temp_addr = interfaces;
        while (temp_addr != nullptr) {
            if (temp_addr->ifa_addr && temp_addr->ifa_addr->sa_family == AF_INET) {
                std::string interfaceName(temp_addr->ifa_name);
                std::string ipAddress(inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr));

                // Only show non-localhost addresses
                if (ipAddress != "127.0.0.1") {
                    std::cout << "Interface: " << interfaceName << " | IP Address: " << ipAddress << std::endl;
                }
            }
            temp_addr = temp_addr->ifa_next;
        }
    }
    freeifaddrs(interfaces);
    return 0;
}

