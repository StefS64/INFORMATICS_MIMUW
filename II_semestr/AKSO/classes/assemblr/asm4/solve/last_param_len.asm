; Wykonanie programu rozpoczyna się od etykiety _start.
global _start

%include "macro_print.asm"

section .text
SYS_WRITE equ 1
SYS_EXIT equ 60
STDOUT equ 1
MAXLEN   equ 17

_start:
  mov   rcx, [rsp]          ; Ładuj do rcx liczbę argumentów.
  mov   rbx, [rsp + 8*rcx]  ; Ładuj do rbx adres ostatniego argumentu.

  xor   al, al              ; Ten kod działa jak strnlen(rdi, MAXLEN).
  cld                       ; Szukaj w kierunku większych adresów.
  mov   ecx, MAXLEN + 1
  mov   rdi, rbx
  repne scasb

  setz  dl
  movzx rdx, dl

  sub   rdi, rbx
  sub   rdi, 1

  print "len =         ", rdi
  print "znacznik ZF = ", rdx
	mov r11, rdi
	mov eax, SYS_WRITE
	mov edi, STDOUT
	lea rsi, [rbx]
	mov rdx, r11
	syscall
;print "name = ", [rbx]	

  mov   eax, SYS_EXIT
  xor   edi, edi
  syscall
