#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <mqueue.h>
#include <string.h>
#include <errno.h>
#include <signal.h>

struct master_state {
    mqd_t *to_master;
    mqd_t *to_slave;
    mqd_t *kill;
    int num_instances;
    const char** instance_names;
};

struct slave_state {
    mqd_t to_master;
    mqd_t to_slave;
    mqd_t kill;
};

void check_error(const int ret, const char *msg) {
    if (ret) {
        perror(msg);
        exit(1);
    }
}

void init_master_queues(struct master_state *state, const char **instance_names, const int num_instances) {
    char name[100];
    
    state->num_instances = num_instances;
    state->instance_names = instance_names;

    state->to_master = malloc(sizeof(mqd_t) * num_instances);
    check_error(state->to_master == NULL, "malloc");

    state->to_slave = malloc(sizeof(mqd_t) * num_instances);
    check_error(state->to_slave == NULL, "malloc");

    state->kill = malloc(sizeof(mqd_t) * num_instances);
    check_error(state->kill == NULL, "malloc");

    for (int i = 0; i < num_instances; i++) {

        // TODO: implement

    }
}

void init_slave_queues(struct slave_state *state, const char *instance_name) {
    
    // TODO: implement

}

struct callback_data {
    mqd_t queue;
    const char *instance_name;
};

void callback(__sigval_t sv) {
    struct callback_data *data = (struct callback_data *) sv.sival_ptr;
    char msg[8192];
    int ret;
    struct sigevent sigev;

    // TODO: implement

    while (1) {

        // TODO: implement

        printf("%s: %s", data->instance_name, msg);
        fflush(stdout);
    }
}

void register_master_callbacks(struct master_state *state, const char **instance_names, const int num_instances) {
    struct sigevent sigev;
    struct callback_data *data;

    for (int i = 0; i < num_instances; i++) {
        data = malloc(sizeof(struct callback_data));
        check_error(data == NULL, "malloc");
    
        // TODO: implement

    }
}

void register_slave_callbacks(struct slave_state *state, const char *instance_name) {
    struct sigevent sigev;
    struct callback_data *data;

    data = malloc(sizeof(struct callback_data));
    check_error(data == NULL, "malloc");

    // TODO: implement

}

void init_master(struct master_state *state, const char **instance_names, const int num_instances) {
    init_master_queues(state, instance_names, num_instances);
    register_master_callbacks(state, instance_names, num_instances);
}

void init_slave(struct slave_state *state, const char *instance_name) {
    init_slave_queues(state, instance_name);
    register_slave_callbacks(state, instance_name);
}

void do_run_master(struct master_state *state) {
    char* msg = NULL;
    size_t len;
    int ret;

    while (1) {
        ret = getline(&msg, &len, stdin);        
        check_error(ret == -1, "getline");

        if (strncmp(msg, "kill", 4) == 0) {

            // TODO: implement

            break;
        }

        // TODO: implement
    }

    free(msg);
}

void do_run_slave(struct slave_state *state) {
    char* msg = NULL;
    size_t len;
    int ret;

    while (1) {
        ret = getline(&msg, &len, stdin);        
        check_error(ret == -1, "getline");

        if (strncmp(msg, "kill", 4) == 0) {
            break;
        }

        // TODO: implement

    }

    free(msg);
}

void cleanup_master(struct master_state *state) {

    // TODO: implement

}

void cleanup_slave(struct slave_state *state) {

    // TODO: implement
    
}

void run_master(const char **instance_names, const int num_instances) {
    struct master_state state;
    init_master(&state, instance_names, num_instances);
    do_run_master(&state);
    cleanup_master(&state);
}

void run_slave(const char *instance_name) {
    struct slave_state state;
    init_slave(&state, instance_name);
    do_run_slave(&state);
    cleanup_slave(&state);
}

int main(const int argc, const char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <mode> [instance names]\n", argv[0]);
        exit(1);
    }

    const int num_instances = argc - 2;
    const char **instance_names = argv + 2;
    const int mode = atoi(argv[1]);
    
    if (mode == 0) {
        run_master(instance_names, num_instances);
    } else {
        run_slave(instance_names[0]);
    }

    return 0;
}