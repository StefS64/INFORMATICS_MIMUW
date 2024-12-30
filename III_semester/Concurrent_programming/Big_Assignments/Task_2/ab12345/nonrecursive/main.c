#include <stddef.h>
#include "common/io.h"
#include "common/sumset.h"
#include <stdio.h>//do usuniecia
#include "stack.h"
#include <stdlib.h>

int main()
{
    InputData input_data;
    input_data_read(&input_data);

    Solution best_solution;
    solution_init(&best_solution);

    Stack s;
    init(&s, 4);

    elem_state input;
    Sumset* a_first = (Sumset*)malloc(sizeof(Sumset));
    Sumset* b_first = (Sumset*)malloc(sizeof(Sumset));
    *a_first = input_data.a_start;
    *b_first = input_data.b_start;
    input.A_power = a_first;
    input.B_power = b_first;
    input.new = true;

    push(input, s);
    int counter = 0;
    int on_stack = 1;
    while (!isEmpty(s)) {
        counter++;
        elem_state top = pop(s);
        if(top.new) {
            top.new = false;
            push(top, s);
            Sumset* a = top.A_power;
            Sumset* b = top.B_power;
            if (a->sum > b->sum) {
                a = top.B_power;
                b = top.A_power;
            }

            if (is_sumset_intersection_trivial(a, b)) { // s(a) ∩ s(b) = {0}.
                for (size_t i = a->last; i <= input_data.d; ++i) {
                    if (!does_sumset_contain(b, i)) {
                        Sumset* a_with_i = (Sumset*)malloc(sizeof(Sumset));
                        sumset_add(a_with_i, a, i);
                        elem_state new_state;
                        new_state.new = true;
                        new_state.A_power = a_with_i;
                        new_state.B_power = b;

                        on_stack++;
                        push(new_state, s);
                    }
                    printf("test\n");
                }
            } else if ((a->sum == b->sum) && (get_sumset_intersection_size(a, b) == 2)) { // s(a) ∩ s(b) = {0, ∑b}.
                if (b->sum > best_solution.sum) {
                    printf("test2\n");
                    solution_build(&best_solution, &input_data, a, b);
                    printf("test3\n");
                    solution_print(&best_solution);
                }
            }
            printf("ile ze stosu sciagnięte %d, było na stosie %d\n", counter, on_stack);\
        } else {
            free(top.A_power);
        }
    }
    free(b_first);
    printf("fajrant\n");
    destroy(s);

    solution_print(&best_solution);
    return 0;
}
