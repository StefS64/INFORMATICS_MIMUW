#ifndef STACK_H
#define STACK_H

#include <pthread.h>
#include <stdatomic.h>

#include "../common/sumset.h"
#include "../common/err.h"

typedef struct sumset_wrapper {
    Sumset power_set;
    atomic_int counter;
    struct sumset_wrapper* parent;
} sumset_wrapper;


typedef struct {
    sumset_wrapper *a_wrap, *b_wrap;
} elem_state;

typedef struct stack_array {
    int max_capacity;
    int stack_top;
    int waiting_threads;
    bool stop;
    elem_state *array;
    pthread_mutex_t mutex;
    pthread_cond_t wait_for_stack;
} stack_array;

typedef struct stack_array* Stack;


void init(Stack* s, int capacity);
int num_threads_waiting_for_stack(Stack s);
bool should_stop(Stack s, int thread_num);
// bool isEmpty(Stack s);
void push(Stack s, elem_state elem);
void pop(Stack s, elem_state *elem);
void stop(Stack s);
void destroy(Stack s);


#endif