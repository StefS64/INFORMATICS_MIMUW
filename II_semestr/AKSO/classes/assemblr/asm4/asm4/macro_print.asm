%ifndef MACRO_PRINT_ASM
%define MACRO_PRINT_ASM

; Wyjątkowo nie definiujemy tu żadnych stałych, żeby nie było konfliktu ze stałymi
; zdefiniowanymi w pliku włączającym ten plik.


; Makro wypisuje napis podany jako pierwszy argument, a potem szesnastkowo zawartość
; rejestru podanego jako drugi argument i kończy znakiem nowej linii.
; Nie modyfikuje zawartości żadnego rejestru ogólnego przeznaczenia ani rejestru
; znaczników.
%macro print 2
  jmp     %%begin
%%descr: db %1                    ; Tu wśród instrukcji makra wkładamy napis podany do makra.
%%begin:
  push    %2                      ; Wartość do wypisania będzie na stosie. To działa również dla %2 = rsp.
  lea     rsp, [rsp - 16]         ; Zrób miejsce na stosie na bufor. Nie modyfikuj znaczników.
  ; Zapamietujemy oryginalną wartość rejestrów, flag.
  pushf
  push    rax
  push    rcx
  push    rdx
  push    rsi
  push    rdi
  push    r11

  ; Teraz stos wygląda tak:
  ; rsp + 80: wierzchołek stosu przed wejściem do makra
  ; rsp + 72: wartość rejestru %2 podanego do marka (8 bajtów)
  ; rsp + 56: bufor na znaki cyfr (16 bajtów, najmniej znacząca cyfra w bajcie rsp + 56 + 15)
  ; rsp + 48: rflags
  ; rsp + 40: rax
  ; rsp + 32: rcx
  ; rsp + 24: rdx
  ; rsp + 16: rsi
  ; rsp +  8: rdi
  ; rsp     : r11

  ; Syscall write(STDOUT, %%descr, %%begin - %%descr)
  mov   edi, 1   ; STDOUT
 
  ; Wypisz do bufora [rsp+56, ..., rsp+56+15] kolejne cyfry liczby [rsp+72].

  mov [rsp + 56 + 16], `\n`

  ; Syscall write(STDOUT, bufor, długość 17)
 
    ; Przywracamy rejestry, flagi, stos.
  pop     r11
  pop     rdi
  pop     rsi
  pop     rdx
  pop     rcx
  pop     rax
  popf
  lea     rsp, [rsp + 24]         ; Popujemy bufor (16 bajtów) oraz wartość %2 (8 bajtów).
%endmacro

%endif
