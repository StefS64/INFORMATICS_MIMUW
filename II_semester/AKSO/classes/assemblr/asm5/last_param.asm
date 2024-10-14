global _start

%include "macro_print.asm"

section .text

SYS_EXIT equ 60
MAXLEN   equ 17

_start:
  mov   rcx, [rsp]          ; rcx := liczba argumentów.
  mov   rbx, [rsp + 8*rcx]  ; rbx := adres ostatniego argumentu.

  ; Ten kod działa jak strlen(rdi, MAXLEN).
  xor   al, al              ; Szukaj znaku 0.
  cld                       ; Szukaj w kierunku większych adresów (CLear Direction flag).
  mov   ecx, MAXLEN + 1     ; Szukaj w MAXLEN + 1 znakach.
  mov   rdi, rbx            ; Szukaj od tego adresu.
  repne scasb
  ; Teraz ZF wskazuje czy znak został znaleziony,
  ; jeśli tak, to rdi wzkazuje na znak **po** znalezionej wartości.

  ; TODO print "znacznik ZF = ", ...

  ; TODO print "len =         ", ...

  ; Syscall exit(0).
  mov   eax, SYS_EXIT
  xor   edi, edi
  syscall
