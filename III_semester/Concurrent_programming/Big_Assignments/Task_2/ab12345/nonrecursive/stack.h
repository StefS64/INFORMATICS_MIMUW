#ifndef STACK_H
#define STACK_H

#include <stddef.h>

#include "../common/sumset.h"

typedef struct {
    Sumset *A_power, *B_power;
    bool new;
} elem_state;

typedef struct stack_array {
    int max_capacity;
    int stack_top;
    elem_state *array;
} stack_array;

typedef struct stack_array* Stack;


void init(Stack* s, int capacity);
bool isEmpty(Stack s);
void push(elem_state elem, Stack s);
elem_state pop(Stack s);
void destroy(Stack s);


#endif