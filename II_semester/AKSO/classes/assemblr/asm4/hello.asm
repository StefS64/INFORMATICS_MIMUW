global _start

SYS_WRITE equ 1
SYS_EXIT  equ 60
STDOUT    equ 1

section .rodata

hello: db `Hello World!\n`
HELLO_LEN equ $ - hello

section .text

_start:
; wywołać write(STDOUT, hello, HELLO_LEN)
mov eax, SYS_WRITE
mov rdi, STDOUT
lea rsi, [rel hello]; hello wzgledne obecnej instrukcji
mov edx, HELLO_LEN
syscall

;Wołamy SYS_EXIT(0) - wszystko sie udało
mov rax, SYS_EXIT; po sys exit nic nie wraca
xor rdi, rdi ; zerujemy rdi
syscall






