global _start

SYS_EXECVE equ 59
SYS_EXIT   equ 60

section .text

_start:
  mov     rcx, [rsp]  ; Ładuj do rcx liczbę parametrów.
  xor     edi, edi    ; Jeśli nie podano parametrów, to program ma się zakończyć kodem 0.
  cmp     rcx, 1
  jbe     exit        ; Nie podano parametrów.

  mov     eax, SYS_EXECVE
  lea     rsi, [rsp + 16]         ; Ładuj adres tablicy parametrów programu do wywołania: rsi = &argv[1].
  lea     rdx, [rsp + 8*rcx + 16] ; Ładuj adres tablicy ze zmiennymi środowiskowymi:      rdx = envp.
  mov     rdi, [rsi]              ; Ładuj nazwę programu do wywołania:                    rdi = argv[1].
  syscall

  ; Jeśli wywołanie SYS_EXECVE się powiodło, to poniższy kod się już nie wykona.
  ; Jeśli jednak doszliśmy tu, to znaczy, że wystąpił błąd - nie trzeba tego sprawdzać.
  ; Kończymy program, sygnalizując błąd.
  mov     edi, 1

exit:
  mov     eax, SYS_EXIT
  syscall
