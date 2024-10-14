#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "list.h"

typedef struct node node;
typedef struct list list;

// Creates a new list in case malloc failing it returns NULL.
list *create_list() {
   list *res = (list *)malloc(sizeof(list));

   if (!res) {
      errno = ENOMEM;
      return NULL;
   }

   res->head = (node *)malloc(sizeof(node));

   if (!res->head) {
      free(res);
      errno = ENOMEM;
      return NULL;
   }

   res->tail = (node *)malloc(sizeof(node));

   if (!res->tail) {
      free(res->head);
      free(res);
      errno = ENOMEM;
      return NULL;
   }

   res->tail->prev = res->head;
   res->head->next = res->tail;
   res->tail->next = NULL;
   res->head->prev = NULL;

   return res;
}


static node *create_node(int gate_number, nand_t *nand_pointer) {
   node *new_node = (node *)malloc(sizeof(node));

   if (!new_node) {
      errno = ENOMEM;
      return NULL;
   }

   *new_node = (node){
   .next = NULL,
   .prev = NULL,
   .connected_nand_gate = nand_pointer,
   .number_of_connection_gate = gate_number
   };

   return new_node;
}


node *kth_element_of_list(int k, list const *l) {
   node *temp = l->head;

   while (k > 0) {
      temp = temp->next;
      k--;
   }

   return temp->next;
}

// Adds a new element at the end of the list.
node *list_add(int gate_number, nand_t *nand_gate_pointer, list *l) {
   node *new_element = create_node(gate_number, nand_gate_pointer);

   if (new_element) {
      node *previous_element = l->tail->prev;

      new_element->prev = previous_element;
      new_element->next = l->tail;

      previous_element->next = new_element;
      l->tail->prev = new_element;
   }
   else {
      return NULL;
   }

   return new_element;
}

// Deletes the element pointed by "node_to_be_deleted", then connects neighbouring pointers.
bool delete_node_and_connect(node *node_to_be_deleted) {
   if (node_to_be_deleted) {
      node *previous = node_to_be_deleted->prev;
      node *following = node_to_be_deleted->next;

      previous->next = following;
      following->prev = previous;

      free(node_to_be_deleted);

      return true;
   }
   else {
      return false;
   }
}


void destroy_list(list *l) {
   if (l) {
      node *temp = l->head;
      node *to_delete;

      while (temp != l->tail) {
         to_delete = temp;
         temp = temp->next;
         free(to_delete);
      }
      free(l->tail);
      free(l);
   }
}
