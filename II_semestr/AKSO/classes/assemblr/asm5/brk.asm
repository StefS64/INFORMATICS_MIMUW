%include "macro_print.asm"

global _start

; Wywołania systemowe.
SYS_MMAP  equ 9
SYS_BRK   equ 12
SYS_EXIT  equ 60

; Opcje do parametrów wywołania SYS_MMAP.
PROT_READ     equ 0x1
PROT_WRITE    equ 0x2
MAP_PRIVATE   equ 0x02
MAP_ANONYMOUS equ 0x20


section .text


_start:
  ; x := syscall brk(0).
  xor     edi, edi
  mov     eax, SYS_BRK
  syscall
  print   "heap end=", rax
	
	add     rbx, 1024
    mov     edi, rbx
    mov     eax, SYS_BRK
    syscall

  ; syscall brk(x + 1024).
  ; TODO
  print   "heap end=", rax

  ; Nieskończona pętla czytająca [rax++].
  ; TODO

  ; syscall exit(0).
  xor     edi, edi
  mov     eax, SYS_EXIT
  syscall

