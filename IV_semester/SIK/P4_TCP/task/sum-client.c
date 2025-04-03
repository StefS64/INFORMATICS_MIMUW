#include <endian.h>
#include <inttypes.h>
#include <netdb.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <string.h>
#include <stdlib.h>
#include "sum-common.h"
#include "err.h"
#include "common.h"

int main(int argc, char *argv[]) {
    if (argc != 5) {
        fatal("usage: %s <host> <port> <number> <length>\n", argv[0]);
    }

    char const *host = argv[1];
    uint16_t port = read_port(argv[2]);
    struct sockaddr_in server_address = get_server_address(host, port);
    int n = atoi(argv[3]);
    int k = atoi(argv[4]);
    char str[k];
    memset(str, '1', k); 

    // Create a socket.
    int socket_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_fd < 0) {
        syserr("cannot create a socket");
    }

    // Connect to the server.
    if (connect(socket_fd, (struct sockaddr *) &server_address,
                (socklen_t) sizeof(server_address)) < 0) {
        syserr("cannot connect to the server");
    }


    for (int i = 0; i < n; i++) {
        // sleep(1);
        printf("sending number %d, with  %ld bytes\n", i,sizeof str);


        ssize_t written_length = write(socket_fd, &str, k);
        if (written_length < 0) {
            syserr("writen");
        }
        // Here we can convert the type, because we know, that written_length >= 0.
        else if ((size_t) written_length != sizeof str) {
            fatal("incomplete writen");
        }

    }

    close(socket_fd);
    return 0;
}
