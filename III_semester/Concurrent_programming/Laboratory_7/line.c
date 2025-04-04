#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include "err.h"

#define BUFFER_SIZE 7

int main(int argc, char* argv[])
{
    int n_children = 5;
    if (argc > 2)
        fatal("Expected zero or one arguments, got: %d.", argc - 1);
    if (argc == 2)
        n_children = atoi(argv[1]);

    pid_t pid;
    n_children--;
    ASSERT_SYS_OK(pid = fork());
    
    if(pid != 0) {
        ASSERT_SYS_OK(wait(NULL));    
    } else if (n_children > 0) {
        printf("My pid is %d, my parent's pid is %d\n", getpid(), getppid());
        char buffer[BUFFER_SIZE];
        int ret = snprintf(buffer, sizeof buffer, "%d", n_children);
        if (ret < 0 || ret >= (int)sizeof(buffer))
            fatal("snprintf failed");
        execlp(argv[0], argv[0], buffer, NULL);
    }
    printf("My pid is %d, exiting.\n", getpid());

    return 0;
}
