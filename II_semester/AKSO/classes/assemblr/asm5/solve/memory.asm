; To jest rozwiązanie ćwiczenia 2.

; Stosując wyszukiwanie binarne, program sprawdza,
; ile pamięci wirtualnej można alokować za pomocą SYS_MMAP.

%include "macro_print.asm"

global _start

; Z tych funkcji systemowych korzystamy.
SYS_MMAP    equ 9
SYS_MUNMAP  equ 11
SYS_EXIT    equ 60

; To są parametry wywołania SYS_MMAP.
PROT_READ     equ 0x1
PROT_WRITE    equ 0x2
MAP_PRIVATE   equ 0x02
MAP_ANONYMOUS equ 0x20

_start:
  ; Pilnujemy niezmiennika r14 <= 15, r14 < 2^63, r15 < 2^63.
  mov     r14, 1                  ; początek przedziału wyszukiwania
  mov     r15, 0x7fffffffffffffff ; koniec przedziału wyszukiwania

  xor     edi, edi                          ; addr = NULL
  mov     edx, PROT_READ | PROT_WRITE       ; prot
  mov     r10d, MAP_PRIVATE | MAP_ANONYMOUS ; flags
  mov     r8d, -1                           ; fd = -1
  xor     r9, r9                            ; offset = 0

binary_search:
  lea     rsi, [r14 + r15 + 1]
  shr     rsi, 1                            ; length
  mov     eax, SYS_MMAP
  syscall
  cmp     rax, -4096
  ja      lower_half
  mov     r14, rsi
  print   "mmap addr ", rax
  print   "mmap size ", rsi
  mov     rdi, rax                          ; addr
  mov     eax, SYS_MUNMAP
  syscall
  cmp     rax, -4096
  ja      exit1
  cmp     r14, r15
  jne     binary_search

exit0:
  xor     edi, edi
  mov     eax, SYS_EXIT
  syscall

exit1:
  neg     rax
  print   "errno     ", rax
  mov     edi, 1
  mov     eax, SYS_EXIT
  syscall

lower_half:
  lea     r15, [rsi - 1]
  jmp     binary_search
