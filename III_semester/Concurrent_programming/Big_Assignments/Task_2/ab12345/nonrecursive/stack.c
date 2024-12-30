#include <stddef.h>
#include <stdlib.h>
#include <stdbool.h>
#include "stack.h"

void init(Stack* s, int capacity) {
    *s = (Stack)malloc(sizeof(stack_array));
    if (*s == NULL) {
        exit(EXIT_FAILURE);
    }
    (*s)->max_capacity = capacity;
    (*s)->stack_top = -1;
    (*s)->array = (elem_state*)malloc(capacity * sizeof(elem_state));
    
    if ((*s)->array == NULL) {
        exit(EXIT_FAILURE);
    }
}

bool isEmpty(Stack s) {
    return s->stack_top == -1;
}

void push(elem_state elem, Stack s) {
    if (s->stack_top == s->max_capacity - 1) {
        s->max_capacity = 2*s->max_capacity;
        s->array = (elem_state*)realloc(s->array, s->max_capacity * sizeof(elem_state));
        if (s->array == NULL) {
            exit(EXIT_FAILURE);
        }
    }
    s->array[++s->stack_top] = elem;
}

elem_state pop(Stack s) {
    if (isEmpty(s)) {
        // Nigdy nie zajdzie
        //printf("STACK EMPTY ERROR");
        exit(EXIT_FAILURE);
    } else {
        return s->array[s->stack_top--];
    }
}

void destroy(Stack s)
{
	if(s != NULL)
	{
		free(s->array);
		free(s);
	}
}