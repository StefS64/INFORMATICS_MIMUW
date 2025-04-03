global sem_init
global sem_acquire
global sem_release

section .text

; void sem_init(custom_sem_t* sem, int initial_value);
sem_init:
    ; TODO: Implement.
    mov [rdi], esi
    ret
; void sem_acquire(custom_sem_t* sem, int amount);
sem_acquire:
    ; TODO: Implement.
    acquire_loop:
        mov eax, [rdi]
        sub eax, esi
        js acquire_loop
        xadd [rdi], eax
        test eax, eax
        js acquire_loop
    ret

; void sem_release(custom_sem_t* sem, int amount);
sem_release:
    ; TODO: Implement.
    lock add [rdi], esi
    ret
