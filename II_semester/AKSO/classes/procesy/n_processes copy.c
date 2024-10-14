#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
const int NUM_PROC = 200;
int main()
{
    pid_t pid;
    for(int i = 0 ; i < NUM_PROC; i++){
        pid = fork();
        if(pid == 0){
            break;
        }else if( pid == -1){
            printf("error\n");
        }
    }        
    if(pid == 0){
            //child process
            sleep(0.5);
            printf("Child: my pid is %d, my parent's pid is %d.\n", getpid(), getppid());
            printf("Child: fork returned %d.\n", pid);
            return 0;
    }
    for(int i = 0; i < NUM_PROC; i++){
        pid_t child_pid = wait(NULL);
        printf("Parent: my pid is %d, my child's pid is %d.\n", getpid(), child_pid);
    }
    return 0;
}