global _start

section .bss

buffer resb 65956                      ; Fragment na którym liczymy crc(bez dziur)
                                       ; ma on wielkość 2^16 by móc wczytać cały fragment na raz
block_length      resb 2               ; Dlugosc fragmentu (bez dziur)
shift             resb 4               ; Dlugosc skoku po fragmencie
crc_poly          resq 1               ; Wielomian
crc_poly_len      resb 1               ; Dlugosc wielomianu
file_descriptor   resq 1               ;   
crc_poly_out      resb 65              ; Zapis wyniku w stringu

section .text
; Wywołania systemowe
SYS_EXIT    equ 60
SYS_READ    equ 0
SYS_OPEN    equ 2
SYS_CLOSE   equ 3
SYS_WRITE   equ 1
SYS_LSEEK   equ 8
READ_ONLY   equ 0
SEEK_CUR    equ 1
STDOUT      equ 1

; Podany program liczy crc pliku z dziurami.
; Czytając plik pierwsze dwa bajty oznaczają długość fragmentu bez dziur
; Po nich znajduję się fragment o po fragmnecie ostatnie 4 bity oznaczają,
; przesunięcie wskaźnika czytającego plik.
; Program się kończy gdy wskaźnik przesunięcia wskazuję na początek fragmentu.
; Crc pliku jest liczone wspólnie dla wszystkich fragmentów traktując je, 
; jako jeden długi nie dziurawy ciąg bitów.
; Program przyjmuję ścieżkę do pliku oraz wielomian crc zapisany w ascii,
; jako pierwszy i drugi argument.
; Wynik w przypadku poprawnego wykonania się, wypisuję na wyjście standardowe.



_start:
   mov      rcx, [rsp]                 ; Wkłada do rcx liczbę argumentów
   cmp      rcx, 3                     ; Sprawdza czy liczba argumentów jest poprawna
   jne      _error
   mov      r10, [rsp + 16]            ; Wczytujemy sciezke pliku
   mov      rdi, [rsp + 24]            ; Zapisuje adres cyrc_poly


; Parsuje podanego stringa i zapisuje go w rax
; Zapisuje również długość wielomiany w crc_poly_len
.parse_string_poly_crc:
   movzx    rbx, byte [rdi]            ; Czytamy pierwszy znak
   cmp      bl, 0                      ; Sprawdza czy jest koniec linii
   je       .poly_crc_written          ; Jeżeli tak kończy działanie
   sub      bl, '0'                    ; Zmienia ASCII na wartosc
   cmp      bl, 1                      ; Sprawdza poprawnosc 0 <= bl <= 1 (czytamy unsigned)
   ja       .input_crc_poly_error
   shl      rax, 1                     ; Przesuwa do bitu rozpatrzanego
   or       al, bl                     ; Zapala odpowiedni bit w rax
   inc      rdi
   inc      byte [rel crc_poly_len]    ; Zapisuje długosc wielomianu
   jmp      .parse_string_poly_crc
.poly_crc_written:
   mov      [rel crc_poly], rax        ; Zapisujemy wielomian crc   


   ; Przed otwarciem pliku sprawdzamy czy długość wielomianuu jest niezerowa.
   ; Rejstr r12 ustawiamy tak aby
   ; zapalony  był bit na pozycji crc_poly_len-1 licząc od najmniej,
   ; znaczących bitów.
   mov      r12, 1                     ; Ustawiamy r12 jako pierwszy zapalony bit
   mov      cl, byte [rel crc_poly_len]
   cmp      cl,0
   je       .empty_poly_error
   dec      cl
   shl      r12, cl;                   ; Przesuwamy o crc_poly_len-1



   ; Otwieramy plik
   mov      rax, SYS_OPEN
   mov      rdi, r10                   ; Przekazujemy ścieżkę pliku
   mov      rdx, READ_ONLY
   mov      rsi, 0
   syscall
   test     rax, rax                   ; Sprawdza czy operacja powiodła się
   js       .open_failed
   mov      [rel file_descriptor], rax ; Zapisujemy file descriptor


   ; Czyta 2 pierwsze bajty pliku, zapsisuje długość fragmentu,
   ; nastepnie zapisuje fragment do buffera (długość buffera stworzona tak,
   ; by móc trzymać cały fragment). Odpala na fragmencie crc.
   ; Ostatecznie czyta długość skoku w 4 bajtach i jeżeli skok wskzazuje,
   ; na początek fragmentu. Wynik crc fragmentu zapisujemy w  r9;
   xor      r9, r9                     ; Przygotowujemy rejestr do zapisu
.fragment_loop:
   mov      rax, SYS_READ              ; Wczytujemy długość fragmentu
   mov      rdi, [rel file_descriptor]
   lea      rsi, [rel block_length]   
   mov      rdx, 2                     
   syscall
   cmp      rax, 2
   jb       .read_failed


   ; Wczytujemy buffer
   lea      rsi, [rel buffer]
   mov      rdi, [rel file_descriptor]
   movzx    rdx, word [rel block_length]      
.not_all_read_again:                   ; Jeżeli nie wszystko zostało wczytane ponawiamy wczytanie 
   mov      rax, SYS_READ                  
   syscall
   test     rax, rax
   js       .read_failed
   add      rsi, rax                   ; Przesuwamy pointer o tyle ile wczytaliśmy
   neg      rax                        
   add      rdx, rax                   ; Odejmujemy ile wczytaliśmy
   test     rdx, rdx                   ; Sprawdzamy czy wszystko wczytane
   jnz      .not_all_read_again

   movzx    rdx, word [rel block_length]
   test     rdx, rdx
   jz       .finished_fragment_crc


   ; Liczymy crc dla fragemntu zapisanego w buffer,
   ; iterujemy się po bajcie a następnie bit po bicie.
   ; Przypominam r9 - zapisuje wynik
   ; r12  rejestr z zapalonym crc_poly_len-1 bitem. 
   xor      r11, r11                   ; Trzyma numer rozpatrzanego bajtu z buffera.
.block_loop:
   lea      r8, [rel buffer]
   mov      bl, byte [r8 + r11]        ; Zapis kolejnego bajtu do odczytu
   mov      al, 0x80                   ; Bit mapa rozważanego bitu
.bajt_loop:
   xor      cl, cl
   test     r9,r12                     ; Sprawdzamy czy po przesunięciu potrzebujemy xor
   jnz      .xor_needed          
   inc      cl                         ; Zapisujemy w cl potrzebę zrobienia xora
.xor_needed:
   shl      r9, 1                      ; Przesuwamy wynik
   test     bl, al                     ; Czytamy kolejny bit
   jz       .new_bit_is_zero
   inc      r9                         ; Ustawiamy bit
.new_bit_is_zero:
   test     cl,cl                      ; Sprawdzamy czy była potrzeba na xor
   jnz      .bit_is_0                  ; Jest zatem xorujemy wynik.
   xor      r9, qword [rel crc_poly]
.bit_is_0:
   shr      al, 1
   test     al,al 
   jnz      .bajt_loop                 ; Pętla iterująca się po bitach
   inc      r11
   cmp      r11, rdx
   jne      .block_loop                ; Pętla iterująca się po bajtach

.finished_fragment_crc:


   ; Zapisujemy 4 bajtowy fragment skoku
   mov      rax, SYS_READ
   mov      rdi, [rel file_descriptor]         
   lea      rsi, [rel shift]         
   mov      rdx, 4
   syscall
   cmp      rax, 4
   jb       .read_failed


   ; Przesuwam pointer pliku o skok(shift)
   mov      rax, SYS_LSEEK
   mov      rdi, [rel file_descriptor]
   movsx    rsi, dword [rel shift]
   mov      rdx, SEEK_CUR
   syscall
   test     rax, rax
   js       .seek_failed

   mov      eax, dword [rel shift]     ; Jeżeli przesunięcie jest dodatnie idziemy dalej
   test     eax,eax               
   jns      .fragment_loop

   neg      eax                        ; Sprawdzamy czy wskazuję na samego siebie
   movzx    edx, word [rel block_length]
   add      edx, 6                     ; 6 + block_length = shift ?
   cmp      eax, edx
   jne      .fragment_loop
   mov      rax, 1;
   mov      cl, [rel crc_poly_len]


   ; Przeszliśmy już cały plik i obliczyliśmy crc,
   ; pozostaje doliczyć ostatnie crc_poly_len zer.
.zero_loop:
   xor      al, al               
   test     r9,r12
   jnz      .xor_needed_zero_loop
   inc      al
.xor_needed_zero_loop:
   shl      r9, 1
   dec      cl
   test     al,al
   jnz      .bit_is_0_2                ; Bit rozpatrzany jest jedynką xorujemy wynik.
   xor      r9, qword [rel crc_poly]
.bit_is_0_2:
   test     cl,cl   
   jnz      .zero_loop


   ; Zmieniamy wynik zapisany w r9 na znaki ASCII
   lea      r11, [rel crc_poly_out]
   mov      rcx, r11                
   add      rcx, 64                    ; Pointer na koniec fragmentu
   mov      byte [rcx], `\n`           ; Wpisujemy znak konca linii
   dec      rcx                  
   mov      rbx, r9
.convert_to_binary:
   mov      rdx, rbx
   and      rdx, 1                     ; Bierzemy najbardziej znaczący bit
   add      dl, '0'                    ; Zmieniamy na ASCII
   mov      [rcx], dl                  ; Zapisujemy 
   dec      rcx
   shr      rbx, 1
   cmp      rcx, r11
   jge      .convert_to_binary 


   ; Wypisujemy wynik, czyli crc_poly_len+1 bajtów od 64-crc_poly_len bajtu.
   mov      rax, SYS_WRITE 
   mov      rdi, STDOUT
   movzx    rcx, byte [rel crc_poly_len]
   add      rcx, -64
   neg      rcx                        ; rcx = 64 - crc_poly_len
   movzx    rdx, byte [rel crc_poly_len]
   inc      rdx
   lea      r9 , [rel crc_poly_out]
   lea      rsi, [r9 + rcx] 
   syscall
   test     rax, rax
   js       .write_failed


   mov      rax, SYS_CLOSE
   mov      rdi, [rel file_descriptor]
   syscall
   test     rax, rax
   js .close_failed


   ; Syscall exit(0)
   xor      edi, edi
   mov      eax, SYS_EXIT
   syscall


   ; Miejsce gdzie obsługujemy błedy.
.empty_poly_error:
.close_failed:
.seek_failed:
.read_failed:
.write_failed:
   mov      rax, SYS_CLOSE
   mov      rdi, [rel file_descriptor]
   syscall

.open_failed:
.input_crc_poly_error:
_error:
   mov      eax, SYS_EXIT
   mov      edi, 1
   syscall
