#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <mqueue.h>
#include <string.h>
#include <errno.h>

#define QUEUE_NAME  "/priorities-example"
#define MAX_SIZE    8192

int main() {
    mqd_t mq;
    char buffer[MAX_SIZE + 1];

    mq = mq_open(QUEUE_NAME, O_CREAT | O_RDWR, 0666, NULL);

    if (mq == -1) {
        perror("mq_open");
        exit(1);
    }

    for (int i = 0; i < 10; i++) {
        sprintf(buffer, "Message #%d", i);
        if (mq_send(mq, buffer, strlen(buffer), i / 2) == -1) {
            perror("mq_send");
            exit(1);
        }
    }

    for (int i = 0; i < 10; i++) {
        if (mq_receive(mq, buffer, MAX_SIZE, NULL) == -1) {
            perror("mq_receive");
            exit(1);
        }
        printf("Message received: %s\n", buffer);
    }

    if (mq_close(mq) == -1) {
        perror("mq_close");
        exit(1);
    }

    if (mq_unlink(QUEUE_NAME) == -1) {
        perror("mq_unlink");
        exit(1);
    }

    return 0;
}