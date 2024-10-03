global abi_test
extern mdiv

%macro save 1
    push %1
    mov %1, 0x6969696969696969
%endmacro

%macro restore 1
    pop %1
%endmacro

; zakomentowane CALLEE SAVED
%macro save_all 0
    ; save rax
    save rbx
    ; save rcx
    ; save rdx
    ; save rsi
    ; save rdi
    save rbp
    ; save rsp
    ; save r8
    ; save r9
    ; save r10
    ; save r11
    save r12
    save r13
    save r14
    save r15
%endmacro

%macro restore_all 0
    ; restore rax  ; rax for returning
    restore r15
    restore r14
    restore r13
    restore r12
    ; restore r11
    ; restore r10
    ; restore r9
    ; restore r8
    ; restore rsp
    restore rbp
    ; restore rdi
    ; restore rsi
    ; restore rdx
    ; restore rcx
    restore rbx
    ; restore rax
%endmacro

%macro check_callee 0
    and rax, r15
    and rax, r14
    and rax, r13
    and rax, r12
    ; and rax, r11
    ; and rax, r10
    ; and rax, r9
    ; and rax, r8
    ; and rax, rsp
    and rax, rbp
    ; and rax, rdi
    ; and rax, rsi
    ; and rax, rdx
    ; and rax, rcx
    and rax, rbx
    ; and rax, rax
%endmacro

; int64_t mdiv(int64_t *x, size_t n, int64_t y);
; *x - rdi
;  n - rsi
;  y - rdx
; Nie zmieniamy ich tutaj, przekazujemy
; callee saved mają pozostać nienaruszone, czyli
;
; Callee-Saved Registers: rbp, rbx, r12-r15
; Caller-Saved Registers: rax, rcx, rdx, rsi, rdi, r8-r11

abi_test:
    save_all
    call mdiv
    mov rax, 0x6969696969696969
    check_callee
    restore_all
    ret
