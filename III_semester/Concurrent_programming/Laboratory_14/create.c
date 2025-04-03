#include <mqueue.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>


void wait() {
    printf("Press enter to continue\n");
    getchar();
}

void print_attr(struct mq_attr *attr) {
    printf("Queue attributes:\n");
    printf("Is queue nonblocking? %s\n", attr->mq_flags & O_NONBLOCK ? "yes" : "no");
    printf("Maximum # of messages on queue: %ld\n", attr->mq_maxmsg);
    printf("Maximum message size: %ld\n", attr->mq_msgsize);
    printf("# of messages currently on queue: %ld\n", attr->mq_curmsgs);
}

int main() {
    char name[80];
    struct mq_attr attr;
    mqd_t mqd;

    printf("Enter a queue name: ");
    fgets(name, 80, stdin);
    name[strlen(name) - 1] = '\0';

    mqd = mq_open(name, O_RDWR | O_CREAT | O_EXCL, 0666, NULL);
    if (mqd == -1) {
        perror("mq_open");
        exit(1);
    }

    printf("Queue created\n");
    wait();

    if (mq_getattr(mqd, &attr) == -1) {
        perror("mq_getattr");
        exit(1);
    }

    print_attr(&attr);

    attr.mq_flags |= O_NONBLOCK;
    attr.mq_maxmsg += 10;
    attr.mq_msgsize += 1;

    if (mq_setattr(mqd, &attr, NULL) == -1) {
        perror("mq_setattr");
        exit(1);
    }

    printf("Queue attributes changed\n");

    if (mq_getattr(mqd, &attr) == -1) {
        perror("mq_getattr");
        exit(1);
    }

    print_attr(&attr);
    wait();

    if (mq_close(mqd) == -1) {
        perror("mq_close");
        exit(1);
    }

    printf("Queue closed\n");
    wait();

    if (mq_unlink(name) == -1) {
        perror("mq_unlink");
        exit(1);
    }

    printf("Queue unlinked\n");
    wait();

    return 0;
}