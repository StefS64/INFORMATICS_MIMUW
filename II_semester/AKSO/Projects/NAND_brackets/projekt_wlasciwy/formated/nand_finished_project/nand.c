#include "nand.h"
#include "list.h"

typedef struct element_of_input_gate element_of_input_gate;

struct nand {

  element_of_input_gate *array_of_input_pointers;
  unsigned inputs_size;
  list *output;
  ssize_t output_size;
  ssize_t critical_path_length;
  enum { not_calculated = 0, in_dfs = 1, calculated = 2 } state;
  bool signal_of_nand;

};


struct element_of_input_gate {

  nand_t *connected_nand_gate;
  node *connected_list_element;
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
      .connected_list_element = NULL,
      .connected_bool = NULL,
      .type = undefined
    };
  }

  *new_nand = (nand_t){
    .array_of_input_pointers = new_input_table,
    .inputs_size = n,
    .output = create_list(),
    .output_size = 0,
    .critical_path_length = 0,
    .state = not_calculated,
    .signal_of_nand = false
  };

  // Checks if list allocation was successful.
  if (!new_nand->output) {
    free(new_nand->array_of_input_pointers);
    free(new_nand);
    errno = ENOMEM;
    return NULL;
  }

  return new_nand;
}

// Given a list of outputs,
// the function iterates through the list,
// reseting the values of connected input gates. 
static void disconnect_connected_inputs(list *output) {
  node *iterator = output->head->next;

  while (iterator != output->tail) {
    element_of_input_gate *connected_input = &(
      iterator->connected_nand_gate->array_of_input_pointers[
        iterator->number_of_connection_gate
      ]
      );

    *connected_input = (element_of_input_gate){
      .connected_nand_gate = NULL,
      .connected_list_element = NULL,
      .connected_bool = NULL,
      .type = undefined
    };

    iterator = iterator->next;
  }
}

// Deletes nand gate, and deletes output list elements
// of gates connected to the inputs of this nand.
void  nand_delete(nand_t *g) {
  if (!g) {
    errno = EINVAL;
    return;
  }

  for (unsigned i = 0; i < g->inputs_size; i++) {
    element_of_input_gate ith_gate = g->array_of_input_pointers[i];

    if (ith_gate.type == gate_type) {
      delete_node_and_connect(ith_gate.connected_list_element);
      ith_gate.connected_nand_gate->output_size--;
    }
  }

  free(g->array_of_input_pointers);

  if (g->output) {
    disconnect_connected_inputs(g->output);
    destroy_list(g->output);
  }

  free(g);
}


int  nand_connect_nand(nand_t *g_out, nand_t *g_in, unsigned k) {
  if (!g_out || !g_in || k >= g_in->inputs_size) {
    errno = EINVAL;
    return -1;
  }

  element_of_input_gate *kth_gate = &g_in->array_of_input_pointers[k];
  node *connected_node = list_add(k, g_in, g_out->output);

  if (!connected_node) {
    errno = ENOMEM;
    return -1;
  }

  // If the kth_gate is already connected to a gate, sever the connection.
  if (kth_gate->type == gate_type) {
    delete_node_and_connect(kth_gate->connected_list_element);
    kth_gate->connected_nand_gate->output_size--;
  }

  (*kth_gate) = (element_of_input_gate){
    .connected_nand_gate = g_out,
    .connected_list_element = connected_node,
    .connected_bool = NULL,
    .type = gate_type
  };

  g_out->output_size++;

  return 0;
}


int  nand_connect_signal(bool const *s, nand_t *g, unsigned k) {
  if (!s || !g || k >= g->inputs_size) {
    errno = EINVAL;
    return -1;
  }

  element_of_input_gate *kth_gate = &g->array_of_input_pointers[k];

  // If the kth_gate is already connected to a gate, sever the connection.
  if (kth_gate->type == gate_type) {
    delete_node_and_connect(kth_gate->connected_list_element);
    kth_gate->connected_nand_gate->output_size--;
  }

  kth_gate->connected_bool = s;
  kth_gate->type = bool_type;

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
    element_of_input_gate ith_gate = current_nand->array_of_input_pointers[i];

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
    element_of_input_gate ith_gate = current_nand->array_of_input_pointers[i];

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

  return g->output_size;
}


void *nand_input(nand_t const *g, unsigned k) {
  if (!g || k >= g->inputs_size) {
    errno = EINVAL;
    return NULL;
  }

  element_of_input_gate kth_gate = g->array_of_input_pointers[k];

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
  if (!g || k > g->output_size || k < 0) {
    errno = EINVAL;
    return NULL;
  }

  return kth_element_of_list(k, g->output)->connected_nand_gate;
}