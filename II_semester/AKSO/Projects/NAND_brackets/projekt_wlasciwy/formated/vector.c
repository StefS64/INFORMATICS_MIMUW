#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <errno.h>
#include "vector.h"

typedef struct elem_of_vector elem_of_vector;
typedef struct vector vector;



// Creates a new vector in case malloc failing it returns NULL.
vector *create_vector() {
  vector *new_vector = (vector *)malloc(sizeof(vector));

  if (!new_vector) {
    errno = ENOMEM;
    return NULL;
  }

  *new_vector = (vector){
    .data = NULL,
    .capacity = 0,
    .size = 0
  };

  return new_vector;
}


// Adds a new element at the end of the vector.
bool vector_push(unsigned gate_number, nand_t *nand_gate_pointer, vector *vect) {
  elem_of_vector new_elem = (elem_of_vector){
  .connected_gate_number = gate_number,
  .connected_nand = nand_gate_pointer
  };

  if (vect->size >= vect->capacity) {
    vect->capacity = (vect->capacity == 0) ? 1 : vect->capacity * 2;
    elem_of_vector *new_data_pointer = realloc(vect->data, sizeof(elem_of_vector) * vect->capacity);

    if (!new_data_pointer) {
      errno = ENOMEM;
      vect->capacity /= 2;

      return false;
    }

    vect->data = new_data_pointer;
  }

  vect->size++;
  vect->data[vect->size - 1] = new_elem;

  return true;
}


// Deletes the last element of vector.
static void vector_pop(vector *vect) {
  if (!vect) {
    return;
  }

  if (vect->size <= vect->capacity / 4) {
    vect->capacity = vect->capacity / 2;
    vect->data = realloc(vect->data, sizeof(elem_of_vector) * vect->capacity);
  }

  if (vect->size > 0) {
    vect->size--;
  }
}




void vector_delete_kth_elem(ssize_t k, vector *vect) {
  if (!vect) {
    return;
  }
  vect->data[k] = vect->data[vect->size - 1];
  vector_pop(vect);
}



void vector_destroy(vector *vect) {
  if (!vect) {
    return;
  }

  free(vect->data);
  free(vect);
}

ssize_t vector_get_size(vector *vect) {
  if (!vect) {
    errno = EINVAL;
    return -1;
  }
  return vect->size;
}

void vector_print(vector *vect) {
  for (int i = 0; i < vect->size; i++) {
    printf("%d ", vect->data[i].connected_gate_number);
  }printf("\n");
}


/*

int main() {
  vector *mama = create_vector();
  for (int i = 0; i < 30; i++) {
    vector_push(i, NULL, mama);
  }
  for (int i = 0; i < 10; i++) {
    vector_pop(mama);
  }
  vector_print(mama);
  for (int i = 0; i < 30; i++) {
    vector_push(i, NULL, mama);
  }
  vector_print(mama);
  for (int i = 0; i < 4; i++) {
    vector_delete_kth_elem(i + 10 * i, mama);
  }
  vector_print(mama);
  vector_destroy(mama);
}*/