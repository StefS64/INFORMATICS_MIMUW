#include "nand.h"


typedef struct element_of_input_gate element_of_input_gate;

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
  
  nand_t* temp = vect->data[vect->size-1].connected_nand;
  temp->
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


struct nand {

  element_of_input_gate *input;
  unsigned inputs_size;
  vector *output;
  ssize_t critical_path_length;
  enum { not_calculated = 0, in_dfs = 1, calculated = 2 } state;
  bool signal_of_nand;

};


struct element_of_input_gate {

  nand_t *connected_nand_gate;
  ssize_t connection_gate_num;
  bool const *connected_bool;
  enum { bool_type, undefined, gate_type } type;

};


nand_t *nand_new(unsigned n) {
  nand_t *new_nand = (nand_t *)malloc(sizeof(nand_t));

  if (!new_nand) {
    errno = ENOMEM;
    return NULL;
  }

  element_of_input_gate *new_input_table = malloc(sizeof(element_of_input_gate) * n);

  if (!new_input_table) {
    free(new_nand);
    errno = ENOMEM;
    return NULL;
  }

  for (unsigned i = 0; i < n; i++) {
    new_input_table[i] = (element_of_input_gate){
      .connected_nand_gate = NULL,
      .connection_gate_num = -1,
      .connected_bool = NULL,
      .type = undefined
    };
  }

  *new_nand = (nand_t){
    .input = new_input_table,
    .inputs_size = n,
    .output = create_vector(),
    .critical_path_length = 0,
    .state = not_calculated,
    .signal_of_nand = false
  };

  // Checks if list allocation was successful.
  if (!new_nand->output) {
    free(new_nand->input);
    free(new_nand);
    errno = ENOMEM;
    return NULL;
  }

  return new_nand;
}

// Given a list of outputs,
// the function iterates through the list,
// reseting the values of connected input gates. 
static void disconnect_connected_inputs(vector *output) {
  for (int i = 0; i < output->size; i++) {
    element_of_input_gate *connected_input = &output->data[i].connected_nand->input[output->data[i].connected_gate_number];

    *connected_input = (element_of_input_gate){
      .connected_nand_gate = NULL,
      .connection_gate_num = -1,
      .connected_bool = NULL,
      .type = undefined
    };
  }
}

// Deletes nand gate, and deletes output vector elements
// of gates connected to the inputs of this nand.
void  nand_delete(nand_t *g) {
  if (!g) {
    errno = EINVAL;
    return;
  }

  for (unsigned i = 0; i < g->inputs_size; i++) {
    element_of_input_gate ith_gate = g->input[i];

    if (ith_gate.type == gate_type) {
      vector_delete_kth_elem(ith_gate.connection_gate_num, ith_gate.connected_nand_gate->output);
    }
  }

  free(g->input);

  if (g->output) {
    disconnect_connected_inputs(g->output);
    vector_destroy(g->output);
  }

  free(g);
}

int  nand_connect_nand(nand_t *g_out, nand_t *g_in, unsigned k) {
  if (!g_out || !g_in || k >= g_in->inputs_size) {
    errno = EINVAL;
    return -1;
  }

  element_of_input_gate *kth_gate = &g_in->input[k];
  if (!vector_push(k, g_in, g_out->output)) {
    errno = ENOMEM;
    return -1;
  }

  // If the kth_gate is already connected to a gate, sever the connection.
  if (kth_gate->type == gate_type) {
    vector_delete_kth_elem(kth_gate->connection_gate_num, kth_gate->connected_nand_gate->output);
  }
  else {
    kth_gate->connection_gate_num = g_out->output->size - 1;
  }

  (*kth_gate) = (element_of_input_gate){
    .connected_nand_gate = g_out,
    .connected_bool = NULL,
    .type = gate_type
  };

  return 0;
}


int  nand_connect_signal(bool const *s, nand_t *g, unsigned k) {
  if (!s || !g || k >= g->inputs_size) {
    errno = EINVAL;
    return -1;
  }

  element_of_input_gate *kth_gate = &g->input[k];

  // If the kth_gate is already connected to a gate, sever the connection.
  if (kth_gate->type == gate_type) {
    vector_delete_kth_elem(kth_gate->connection_gate_num, kth_gate->connected_nand_gate->output);
  }

  (*kth_gate) = (element_of_input_gate){
    .connected_nand_gate = NULL,
    .connection_gate_num = -1,
    .connected_bool = s,
    .type = bool_type
  };
  return 0;
}


ssize_t max(ssize_t a, ssize_t b) {
  return (a > b) ? a : b;
}

// In case of any error that occurred earlier function returns -1,
// otherwise it returns the critical path and sets the signal of the nand gate.
static ssize_t set_nand_values(
  ssize_t number_of_true_bits,
  ssize_t max_critical_path,
  nand_t *current
) {
  if (number_of_true_bits == -1) {
    return -1;
  }

  current->signal_of_nand = !(number_of_true_bits == current->inputs_size);
  current->critical_path_length = (current->inputs_size == 0) ? 0 : max_critical_path + 1;

  return current->critical_path_length;
}

// The function runs DFS from "current_nand", in case of any error:
// loops
// undefined gate input.
//
// The function sets "number_of_true_bits" to -1 and errno to adequate error.
// 
// Memory allocation error is not possible.
// 
// If an error occurs the DFS is halted and all states of nand's that are,
// currently in "in_dfs" state are changed to calculated. 
// After that the clean up, function changes their states to not_calculated.
static ssize_t nand_dfs(nand_t *current_nand) {
  current_nand->state = in_dfs;
  ssize_t number_of_true_bits = 0, max_critical_path = 0;


  for (
    unsigned i = 0;
    i < current_nand->inputs_size && number_of_true_bits != -1;
    i++
    ) {
    element_of_input_gate ith_gate = current_nand->input[i];

    switch (ith_gate.type) {
    case bool_type:
      bool *gate_i_bool = nand_input(current_nand, i);

      if (*gate_i_bool) {
        number_of_true_bits++;
      }

      break;

    case gate_type:
      nand_t *gate_i_nand = nand_input(current_nand, i);

      if (gate_i_nand->state == in_dfs) {
        errno = ECANCELED;
        number_of_true_bits = -1;
        break;
      }

      ssize_t critical_path_i = (gate_i_nand->state == calculated)
        ? gate_i_nand->critical_path_length
        : nand_dfs(gate_i_nand);

      if (critical_path_i == -1) {
        number_of_true_bits = -1;
        break;
      }

      max_critical_path = max(max_critical_path, critical_path_i);

      if (gate_i_nand->signal_of_nand) {
        number_of_true_bits++;
      }

      break;

    case undefined:
      errno = ECANCELED;
      number_of_true_bits = -1;
      break;
    }
  }

  current_nand->state = calculated;

  return set_nand_values(number_of_true_bits, max_critical_path, current_nand);
}

// Runs DFS from "current_nand", sets all calculated states to "not_calculated".
static void nand_dfs_clean(nand_t *current_nand) {
  if (current_nand->state != calculated) {
    return;
  }

  current_nand->state = in_dfs;

  for (unsigned i = 0; i < current_nand->inputs_size; i++) {
    element_of_input_gate ith_gate = current_nand->input[i];

    if (ith_gate.type == gate_type) {
      nand_dfs_clean(ith_gate.connected_nand_gate);
    }
  }

  current_nand->state = not_calculated;
}


static void nand_evaluate_clean_up(nand_t **g, size_t m) {
  for (size_t i = 0; i <= m; i++) {
    if (g[i]->state == calculated) {
      nand_dfs_clean(g[i]);
    }
  }
}


static void set_bool_signals(nand_t **g, bool *s, size_t m) {
  for (size_t i = 0; i < m; i++) {
    s[i] = g[i]->signal_of_nand;
  }
}

// For every gate in table "g" this function runs a DFS from that node.
// In case of any error function "nand_dfs" returns -1 and sets errno.
ssize_t nand_evaluate(nand_t **g, bool *s, size_t m) {
  ssize_t critical_path = 0;

  if (m == 0 || !g || !s) {
    errno = EINVAL;
    return -1;
  }

  for (size_t i = 0; i < m; i++) {
    if (!g[i]) {
      errno = EINVAL;
      return -1;
    }
  }

  // All elements of g table are well defined thus,
  // NULL value checks are redundant until this function terminates.
  for (size_t i = 0; i < m; i++) {
    if (g[i]->state == not_calculated) {
      ssize_t ith_critical_path = nand_dfs(g[i]);

      if (ith_critical_path == -1) {
        nand_evaluate_clean_up(g, i);
        return -1;
      }
      else {
        critical_path = max(critical_path, ith_critical_path);
      }
    }
  }

  set_bool_signals(g, s, m);
  nand_evaluate_clean_up(g, m - 1);

  return critical_path;
}


ssize_t nand_fan_out(nand_t const *g) {
  if (!g) {
    errno = EINVAL;
    return -1;
  }

  return vector_get_size(g->output);
}


void *nand_input(nand_t const *g, unsigned k) {
  if (!g || k >= g->inputs_size) {
    errno = EINVAL;
    return NULL;
  }

  element_of_input_gate kth_gate = g->input[k];

  switch (kth_gate.type) {
  case bool_type:
    return (void *)kth_gate.connected_bool;

  case gate_type:
    return (void *)kth_gate.connected_nand_gate;

  case undefined:
    errno = 0;
    return NULL;
  }

  return NULL;
}


nand_t *nand_output(nand_t const *g, ssize_t k) {
  if (!g || k > g->output->size || k < 0) {
    errno = EINVAL;
    return NULL;
  }

  return g->output->data[k].connected_nand;
}