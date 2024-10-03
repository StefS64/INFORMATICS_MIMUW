global _start

SYS_WRITE equ 1
SYS_EXIT  equ 60
STDOUT    equ 1

section .rodata

hello: db `Hello World!\n`
HELLO_LEN equ $ - hello

section .text

_start:
  ; Wywołać write(STDOUT, hello, HELLO_LEN)
  mov eax, SYS_WRITE
  mov edi, STDOUT
  lea rsi, [rel hello]
  mov edx, HELLO_LEN
  syscall

  ; Wołamy SYS_EXIT(0)
  mov rax, SYS_EXIT
  xor rdi, rdi
  syscall
  
