#include <stddef.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include "stack.h"

void init(Stack* s, int capacity) {
    *s = (Stack)malloc(sizeof(stack_array));
    if (*s == NULL) {
        exit(EXIT_FAILURE);
    }
    (*s)->max_capacity = capacity;
    (*s)->stack_top = -1;
    (*s)->array = (elem_state*)malloc(capacity * sizeof(elem_state));
    (*s)->stop = false;
    (*s)->waiting_threads = 0;
    
    if ((*s)->array == NULL) {
        exit(EXIT_FAILURE);
    }
    
    pthread_mutex_init(&(*s)->mutex, NULL);
    pthread_cond_init(&(*s)->wait_for_stack, NULL);
}

static bool isEmpty(Stack s) {
    return s->stack_top == -1;
}


void push(Stack s, elem_state elem) {
    pthread_mutex_lock(&s->mutex);
    printf("push1\n");
    if (s->stack_top == s->max_capacity - 1) {
        s->max_capacity = 2*s->max_capacity;
        s->array = (elem_state*)realloc(s->array, s->max_capacity * sizeof(elem_state));
        if (s->array == NULL) {
            exit(EXIT_FAILURE);
        }
    }
    s->array[++s->stack_top] = elem;
    pthread_cond_signal(&s->wait_for_stack);
    pthread_mutex_unlock(&s->mutex);
}

void pop(Stack s, elem_state* elem) {
    pthread_mutex_lock(&s->mutex);
    printf("pop1\n");
    s->waiting_threads++;
    while (isEmpty(s) && !s->stop) {
        printf("waiting...\n");
        pthread_cond_wait(&s->wait_for_stack, &s->mutex);
    }
    printf("pop2\n");
    s->waiting_threads--;
    if(s->stop) {
        return;
    }
    printf("pop3 top: %d\n", s->stack_top);
    elem_state e = s->array[s->stack_top--];
    printf("pop4\n");
    *elem = e;
    printf("pop4\n");
    pthread_mutex_unlock(&s->mutex);
}

bool should_stop(Stack s, int thread_num) {
    pthread_mutex_lock(&s->mutex);
    printf("should_stop1\n");
    bool ans = false;
    printf("should_stop2: %d, %d ", isEmpty(s), s->waiting_threads);
    if(isEmpty(s) && s->waiting_threads == thread_num-1) {
        ans = true;
    } 
    pthread_mutex_unlock(&s->mutex);
    return ans;
}

void stop(Stack s) {
    pthread_mutex_lock(&s->mutex);
    printf("stop1\n");
    s->stop = true;
    pthread_cond_broadcast(&s->wait_for_stack);
    pthread_mutex_unlock(&s->mutex);
}


void destroy(Stack s)
{
    pthread_mutex_destroy(&s->mutex);
    pthread_cond_destroy(&s->wait_for_stack);
	if(s != NULL)
	{
		free(s->array);
		free(s);
	}
}