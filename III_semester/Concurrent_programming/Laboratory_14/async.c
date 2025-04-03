#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <mqueue.h>
#include <signal.h>
#include <errno.h>
#include <pthread.h>

#define MQNAME "/async-queue"
#define MSG "Hello, world!"
#define MAX_MSG_SIZE 8192

char msg[MAX_MSG_SIZE];

void run_sender() {
    mqd_t mqd = mq_open(MQNAME, O_WRONLY | O_CREAT, 0666, NULL);
    if (mqd == -1) {
        perror("mq_open");
        exit(1);
    }

    if (mq_send(mqd, MSG, strlen(MSG), 0) == -1) {
        perror("mq_send");
        exit(1);
    }

    mq_close(mqd);
}

void notify_callback(__sigval_t sv) {
    printf("Notify callback called with value: %d\n", sv.sival_int);
    printf("Thread ID: %ld\n", pthread_self());
}

void callback(int sig) {
    printf("Signal handler called with signal: %d\n", sig);
    printf("Thread ID: %ld\n", pthread_self());
}

void run_receiver(int sigev) {
    mqd_t mqd;
    struct sigevent sigevp;

    printf("Receiver Thread ID: %ld\n", pthread_self());

    mqd = mq_open(MQNAME, O_RDONLY | O_CREAT, 0666, NULL);
    if (mqd == -1) {
        perror("mq_open");
        exit(1);
    }

    sigevp.sigev_notify = sigev ? SIGEV_THREAD : SIGEV_SIGNAL;
    sigevp.sigev_signo = SIGUSR1;
    sigevp.sigev_value.sival_int = 42;
    sigevp.sigev_notify_function = (void (*)(__sigval_t)) notify_callback;
    sigevp.sigev_notify_attributes = NULL;

    struct sigaction sa;
    sa.sa_handler = callback;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    if (sigaction(SIGUSR1, &sa, NULL) == -1) {
        perror("sigaction");
        exit(1);
    }

    if (mq_notify(mqd, &sigevp) == -1) {
        perror("mq_notify");
        exit(1);
    }

    sigev ? sleep(10) : pause();

    if (mq_receive(mqd, msg, MAX_MSG_SIZE, NULL) == -1) {
        perror("mq_receive");
        exit(1);
    }

    printf("Received message: %s\n", msg);

    mq_close(mqd);
}

int main(int argc, char *argv[]) {
    int sender, sigev;

    if (argc < 2) {
        printf("usage: ./async <0:receiver 1:sender> [0:SIGEV_SIGNAL 1:SIGEV_THREAD]\n");
        exit(1);
    }

    sender = atoi(argv[1]);
    if (argc == 3) {
        sigev = atoi(argv[2]);
    }

    if (sender) {
        run_sender();
    } else {
        run_receiver(sigev);
    }

    return 0;    
}