; Wykonanie programu rozpoczyna się od etykiety _start.
global _start

%include "macro_print.asm"

SYS_EXIT equ 60

section .text

_start:
  mov     rax, rsp
  print   "rsp = ", rsp
  print   "rsp = ", rax
  mov     rax, 0x0123456789abcdef
  print   "rax = ", rax
  mov     rbx, 0xfedcba9876543210
  print   "rbx = ", rbx

  mov     eax, SYS_EXIT
  xor     edi, edi
  syscall
