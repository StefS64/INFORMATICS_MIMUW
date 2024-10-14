#ifndef LIST_H
#define LIST_H
#include "nand.h"
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct node node;

typedef struct node {
   node *next;
   node *prev;
   // Every element of
   nand_t *connected_nand_gate;
   int number_of_connection_gate;
}node;

typedef struct list {
   node *head;
   node *tail;
} list;

ssize_t list_size(list *l);
list *create_list();
node *list_create_node(int gate_number, nand_t *nand_pointer);
node *kth_element_of_list(int k, list const *l);
node *list_add(int gate_number, nand_t *nand_gate_pointer, list *l);
bool delete_node_and_connect(node *deleted_node);
void destroy_list(list *l);
void print_list(list *l);

#endif
