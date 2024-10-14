#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <errno.h>
#include "nand.h"

typedef struct elem_of_vector elem_of_vector;
typedef struct vector vector;

struct elem_of_vector {
  unsigned connected_gate_number;
  nand_t *connected_nand;
};

struct vector {
  elem_of_vector *data;
  ssize_t size;
  ssize_t capacity;
};

// Creates a new vector in case malloc failing it returns NULL.
vector *create_vector();
bool vector_push(unsigned gate_number, nand_t *nand_gate_pointer, vector *vect);
void vector_delete_kth_elem(ssize_t k, vector *vect);
void vector_destroy(vector *vect);
ssize_t vector_get_size(vector *vect);

