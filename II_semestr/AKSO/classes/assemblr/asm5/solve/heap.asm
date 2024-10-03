; To jest rozwiązanie ćwiczenia 1.

; Pamięć jest przydzielana całymi stronami, więc jeśli alokujemy mniej pamięci,
; niż wynosi rozmiar strony, to bez naruszania ochrony pamięci możemy odwoływać
; się do końca strony, która została przydzielona procesowi.

%include "macro_print.asm"

global _start

; Z tych funkcji systemowych korzystamy.
SYS_BRK   equ 12
SYS_EXIT  equ 60

_start:
  xor     edi, edi          ; Poznaj początkowy adres końca sterty.
  mov     eax, SYS_BRK
  syscall
  cmp     rax, -4096        ; Sprawdź, czy w rax jest wartość
  ja      exit1             ; pomiędzy -1 a -4095. Tak to robią w libc.
  print   "heap end0 ", rax


  lea     rdi, [rax + 1024] ; Rozszerz stertę o 1024 bajty.
  mov     eax, SYS_BRK
  syscall
  cmp     rax, -4096        ; Sprawdź, czy w rax jest wartość
  ja      exit1             ; pomiędzy -1 a -4095. Tak to robią w libc.
  print   "heap end1 ", rax

  or      rax, 0xfff        ; Umieść w rax adres końca strony.
  print   "rax       ", rax
  mov     byte [rax], 0     ; To powinno się udać.

  inc     rax               ; Umieść w rax adres początku następnej strony.
  print   "rax       ", rax
  mov     byte [rax], 0     ; To narusza ochronę pamięci.

  xor     edi, edi
  print   "exit      ", rdi
  mov     eax, SYS_EXIT
  syscall

exit1:
  neg     rax               ; Wypisz kod błędu i zakończ program.
  print   "errno     ", rax
  mov     edi, 1
  mov     eax, SYS_EXIT
  syscall
