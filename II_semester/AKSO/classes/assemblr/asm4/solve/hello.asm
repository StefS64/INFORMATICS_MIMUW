; Wykonanie programu zaczyna się od etykiety _start.
global _start

SYS_WRITE equ 1
SYS_EXIT  equ 60
STDOUT    equ 1

section .rodata

hello: db `Hello World!\n`
HELLO_LEN equ $ - hello

section .text

_start:
  ; Użycie rejestrów:
  ;   rax - numer funkcji,
  ;   rdi - deskryptor pliku,
  ;   rsi - wskaźnik na bufor z danymi do wypisania,
  ;   rdx - liczba bajtów.
  ; Operacje na 32-bitowych młodszych połówkach 64-bitowych rejestrów
  ; zerują ich starsze 32-bitowe połówki.
  mov eax, SYS_WRITE
  mov edi, STDOUT
  lea rsi, [rel hello]
  mov edx, HELLO_LEN
  syscall

  ; Jeśli nie wywołamy sys_exit, program naruszy ochronę pamięci.
  ; Użycie rejestrów:
  ;   rax - numer funkcji,
  ;   rdi – kod zakończenia procesu.
  mov eax, SYS_EXIT
  xor edi, edi
  syscall
