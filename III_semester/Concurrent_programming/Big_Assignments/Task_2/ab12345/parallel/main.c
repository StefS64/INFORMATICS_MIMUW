#include <stddef.h>
#include "../common/io.h"
#include "../common/sumset.h"
#include <stdio.h>//do usuniecia
#include "stack.h"
#include <stdlib.h>

int globa_id = 0;

typedef struct ThreadData {
    Stack tasks;
    Solution* best_solution;
    pthread_mutex_t build;
    InputData *original_data;
    int d;
} ThreadData;


// checks all counters of sub wrappers of sumset and deletes the right ones
void destroy_memory(sumset_wrapper* a) {
    sumset_wrapper* check = a;
    while(check != NULL && atomic_fetch_sub(&check->counter, 1) == 1) {
        sumset_wrapper* next_check = check->parent;
        free(check);
        check = next_check;
    }
}

void recursive_solve(elem_state elem, struct ThreadData *data, Solution* local) {
 
    sumset_wrapper* a = elem.a_wrap;
    sumset_wrapper* b = elem.b_wrap;
    if (a->power_set.sum > b->power_set.sum) {
        a = elem.b_wrap;
        b = elem.a_wrap;
    }

    if (is_sumset_intersection_trivial(&a->power_set, &b->power_set)) { // s(a) ∩ s(b) = {0}.
        for (size_t i = a->power_set.last; i <= data->d; ++i) {
            if (!does_sumset_contain(&b->power_set, i)) {
                a->counter++;
                b->counter++;

                sumset_wrapper* a_with_i = (sumset_wrapper*)malloc(sizeof(sumset_wrapper));

                a_with_i->counter = 1;
                a_with_i->parent = a;

                sumset_add(&a_with_i->power_set, &a->power_set, i);

                elem_state new_state;
                new_state.a_wrap = a_with_i;
                new_state.b_wrap = b;

                if(data->tasks->waiting_threads > 0) {// Tutaj wyjaśnić
                    push(data->tasks, new_state);
                } else {
                    recursive_solve(new_state, data, local);
                }
            }
        }
    } else if ((a->power_set.sum == b->power_set.sum) && (get_sumset_intersection_size(&a->power_set, &b->power_set) == 2)) { // s(a) ∩ s(b) = {0, ∑b}.
        if (a->power_set.sum > local->sum) {
            solution_build(local, data->original_data, &a->power_set, &b->power_set); // local_best_solution_build
            // printf("solution after build\n");
            // solution_print(local);

        }
    }
    destroy_memory(a);
    destroy_memory(b);
}



// void before 
void *working_thread(void* data) {
    #ifdef DEBUG
    printf("starting work\n");
    #endif
    ThreadData* calculate = data;
    Solution local;
    solution_init(&local);
    #ifdef DEBUG
    solution_print(&local);
    printf("data obtained\n");
    #endif
    while (true) {
    #ifdef DEBUG
        printf("hello\n");
    #endif
        elem_state  top; 
        pop(calculate->tasks, &top);
    #ifdef DEBUG
        printf("popped stack\n");
    #endif
        if(calculate->tasks->stop) {
            ASSERT_ZERO(pthread_mutex_lock(&calculate->build));
            // solution_print(&local);
            if (calculate->best_solution->sum < local.sum) {
                // printf("change\n");
                *calculate->best_solution = local;
                // solution_print(calculate->best_solution);
            }
            ASSERT_ZERO(pthread_mutex_unlock(&calculate->build));
            #ifdef DEBUG
            printf("stopped thread\n");
            #endif
            break;
        }
    #ifdef DEBUG
        printf("start recursive\n");
    #endif
        recursive_solve(top, calculate, &local);
       
    }

    return 0;
}

void init_thread_data(ThreadData *thread_data, Solution *best_solution, InputData *input_data) {
    

    Stack s;
    init(&s, 4, input_data->t);

    elem_state input;
    
    sumset_wrapper* a_first = (sumset_wrapper*)malloc(sizeof(sumset_wrapper));
    sumset_wrapper* b_first = (sumset_wrapper*)malloc(sizeof(sumset_wrapper));
    
    a_first->power_set = input_data->a_start;
    b_first->power_set = input_data->b_start;

    a_first->counter = 1;
    b_first->counter = 1;

    a_first->parent = NULL;
    b_first->parent = NULL;

    input.a_wrap = a_first;
    input.b_wrap = b_first;
    #ifdef DEBUG
    printf("push_input\n");
    #endif

    // do solution check
    push(s, input);

    thread_data->best_solution = best_solution;
    thread_data->original_data = input_data;
    thread_data->d = input_data->d;
    thread_data->tasks = s;
    
    ASSERT_ZERO(pthread_mutex_init(&thread_data->build, NULL));
}

void thread_data_destroy(ThreadData *data) {
    ASSERT_ZERO(pthread_mutex_destroy(&data->build));
    destroy(data->tasks);
}

int main()
{
    Solution best_solution;
    solution_init(&best_solution);

    InputData input_data;
    input_data_read(&input_data);

    #ifdef DEBUG  
    printf("start\n");
    #endif
    ThreadData thread_data;
    init_thread_data(&thread_data, &best_solution, &input_data);
    #ifdef DEBUG
    printf("Initializing data\n");
    #endif
    int thread_num = thread_data.original_data->t;
    pthread_t threads[thread_num];

    for (int i = 0; i < thread_num; i++) {
    #ifdef DEBUG
        printf("created_thread %d\n", i);
    #endif
        ASSERT_ZERO(pthread_create(&threads[i], NULL, working_thread, &thread_data));
    }

    for (int i = 0; i < thread_num; i++)
        ASSERT_ZERO(pthread_join(threads[i], NULL));
    thread_data_destroy(&thread_data); 
    solution_print(&best_solution);
    return 0;
}
