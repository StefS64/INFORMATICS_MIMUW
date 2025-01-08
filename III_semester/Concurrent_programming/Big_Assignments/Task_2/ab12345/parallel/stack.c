#include <stddef.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include "stack.h"

int pop_counter = 0;
static inline bool should_stop(Stack s);


void init(Stack* s, int capacity, int num_threads) {
    *s = (Stack)malloc(sizeof(stack_array));
    if (*s == NULL) {
        exit(EXIT_FAILURE);
    }
    (*s)->max_capacity = capacity;
    (*s)->stack_top = -1;
    (*s)->array = (elem_state*)malloc(capacity * sizeof(elem_state));
    (*s)->stop = false;
    (*s)->waiting_threads = 0;
    (*s)->t = num_threads;
    
    if ((*s)->array == NULL) {
        exit(EXIT_FAILURE);
    }
    
    ASSERT_ZERO(pthread_mutex_init(&(*s)->mutex, NULL));
    ASSERT_ZERO(pthread_cond_init(&(*s)->wait_for_stack, NULL));
}

static bool isEmpty(Stack s) {
    return s->stack_top == -1;
}


void push(Stack s, elem_state elem) {
    ASSERT_ZERO(pthread_mutex_lock(&s->mutex));
    #ifdef DEBUG
        printf("push1\n");
    #endif
    if (s->stack_top == s->max_capacity - 1) {
        s->max_capacity = 2*s->max_capacity;
        s->array = (elem_state*)realloc(s->array, s->max_capacity * sizeof(elem_state));
        if (s->array == NULL) {
            exit(EXIT_FAILURE);
        }
    }
    s->array[++s->stack_top] = elem;
    ASSERT_ZERO(pthread_cond_signal(&s->wait_for_stack));
    ASSERT_ZERO(pthread_mutex_unlock(&s->mutex));
}

void pop(Stack s, elem_state* elem) {
    ASSERT_ZERO(pthread_mutex_lock(&s->mutex));
    #ifdef COUNT
    pop_counter++;
    printf("popped: %d\n", pop_counter);
    #endif
    #ifdef DEBUG
        printf("pop1\n");
    #endif
     
    if (should_stop(s)) {
        // stop(calculate->tasks);
        #ifdef DEBUG
        printf("stopped last\n");
        #endif
        pthread_mutex_unlock(&s->mutex);
        return;
    }
    #ifdef COUNT
    printf("stack waiting: %d\n", s->waiting_threads);
    #endif
    s->waiting_threads++;
    while (isEmpty(s) && !s->stop) {
    #ifdef DEBUG
        printf("waiting...\n");
    #endif
        pthread_cond_wait(&s->wait_for_stack, &s->mutex);
        #ifdef DEBUG
        printf("poping stack. stop: %d\n", s->stop);
        #endif
    }
    #ifdef DEBUG
    printf("pop2\n");
    #endif
    s->waiting_threads--;
    #ifdef DEBUG
    printf("poping stack: %d\n", s->waiting_threads);
    #endif
    if(s->stop) {
        #ifdef DEBUG
        printf("left stack\n");
        #endif
        ASSERT_ZERO(pthread_mutex_unlock(&s->mutex));
        return;
    }
    #ifdef DEBUG
    printf("pop3 top: %d\n", s->stack_top);
    #endif
    *elem = s->array[s->stack_top--];
    #ifdef DEBUG
    printf("pop4\n");
    #endif
    ASSERT_ZERO(pthread_mutex_unlock(&s->mutex));
}

static inline bool should_stop(Stack s) { // merge with stop

    #ifdef DEBUG
    printf("should_stop1\n");
    #endif
    bool ans = false;
    #ifdef DEBUG
    printf("should_stop2: %d, %d ", isEmpty(s), s->waiting_threads);
    printf("should_stop2, empty_stack: %d, wait: %d \n", isEmpty(s), s->waiting_threads);
    #endif
    if(isEmpty(s) && s->waiting_threads == s->t - 1) {
        ans = true;
        s->stop = true;
        pthread_cond_broadcast(&s->wait_for_stack);
        #ifdef DEBUG
        printf("trying to stop\n");
        #endif
    } 
    return ans;
}


void destroy(Stack s)
{
    ASSERT_ZERO(pthread_mutex_destroy(&s->mutex));
    ASSERT_ZERO(pthread_cond_destroy(&s->wait_for_stack));
	if(s != NULL)
	{
		free(s->array);
		free(s);
	}
}