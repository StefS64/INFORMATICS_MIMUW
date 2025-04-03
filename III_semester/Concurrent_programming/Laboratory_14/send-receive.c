#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <mqueue.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>

#define SERVER_QUEUE_NAME "/send-receive-example"
#define MESSAGE "Hello, world!"
#define MESSAGE_SIZE strlen(MESSAGE)
#define BUFFER_SIZE 8192

char buffer[BUFFER_SIZE];


void wait(const char* msg) {
    printf("Press enter to %s\n", msg);
    getchar();
}

void check_attr(mqd_t mqd, const char* msg) {
    struct mq_attr attr;

    if (mq_getattr(mqd, &attr) == -1) {
        perror("mq_getattr");
        exit(1);
    }

    printf("Queue attributes:\n");
    printf("Is queue nonblocking? %s\n", attr.mq_flags & O_NONBLOCK ? "yes" : "no");
    printf("Maximum # of messages on queue: %ld\n", attr.mq_maxmsg);
    printf("Maximum message size: %ld\n", attr.mq_msgsize);
    printf("# of messages currently on queue: %ld\n", attr.mq_curmsgs);

    wait(msg);
}

void run_server() {
    mqd_t mqd;
    unsigned int prio;

    wait("create the queue");
    mqd = mq_open(SERVER_QUEUE_NAME, O_RDONLY | O_CREAT, 0666, NULL);
    if (mqd == -1) {
        perror("mq_open");
        exit(1);
    }

    wait("check queue attributes");
    check_attr(mqd, "receive a message");

    if (mq_receive(mqd, buffer, BUFFER_SIZE, &prio) == -1) {
        perror("mq_receive");
        exit(1);
    }
    printf("Message received: %s\n", buffer);

    check_attr(mqd, "close the queue");

    if (mq_close(mqd) == -1) {
        perror("mq_close");
        exit(1);
    }

    wait("unlink the queue"); 
    if (mq_unlink(SERVER_QUEUE_NAME) == -1) {
        perror("mq_unlink");
        exit(1);
    }
}

void run_client() {
    mqd_t mqd;

    wait("open the queue");
    mqd = mq_open(SERVER_QUEUE_NAME, O_WRONLY);
    if (mqd == -1) {
        perror("mq_open");
        exit(1);
    }

    check_attr(mqd, "send a message");

    if (mq_send(mqd, MESSAGE, MESSAGE_SIZE, 0) == -1) {
        perror("mq_send");
        exit(1);
    }

    check_attr(mqd, "close the queue");

    if (mq_close(mqd) == -1) {
        perror("mq_close");
        exit(1);
    }

    wait("wait for the server to exit");
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <mode>\n", argv[0]);
        exit(1);
    }

    int mode = atoi(argv[1]);

    if (mode == 0) {
        run_server();
    } else {
        run_client();
    }

    return 0;
}
    
