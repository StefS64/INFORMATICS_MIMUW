global _start

section .bss

buffer resb 65956					   ; Fragment na którym liczymy crc(bez dziur)
block_length resb 2					; Dlugosc fragmentu (bez dziur)
shift resb 4						   ; Dlugosc skoku po fragmencie
crc_poly resq 1						; Wielomian
crc_poly_len resb 1					; Dlugosc wielomianu
file_descriptor resq 1				;	
crc_poly_out resb 65				   ; Zapis wyniku w stringu

section .text
; Wywołania systemowe
SYS_EXIT equ 60
SYS_READ equ 0
SYS_OPEN equ 2
SYS_CLOSE equ 3
SYS_WRITE equ 1
SYS_LSEEK equ 8
READ_ONLY equ 0
SEEK_CUR equ 1
STDOUT equ 1

_start:
	mov rcx, [rel rsp]				; Wkłada do rcx liczbę argumentów
	cmp rcx, 3						   ; Sprawdza czy liczba argumentów jest poprawna
	jne _error
	mov r10, [rel rsp + 16]			; Wczytujemy sciezke pliku
	mov rdi, [rel rsp + 24] 		; Zapisuje adres cyrc_poly
	
.parse_string_poly_crc:				; Parsuje podanego stringa i zapisuje go w rax
	movzx rbx, byte [rel rdi];
	cmp bl, 0		;
	je .poly_crc_written
	sub bl, '0';
	cmp bl, 1
	ja .input_crc_poly_error		; Sprawdza poprawnosc    0 <= bl <= 1
	shl rax, 1;
	or al, bl;
	inc rdi
	inc byte [rel crc_poly_len]	; Zapisuje długosc wielomianu
	jmp .parse_string_poly_crc
.poly_crc_written:

	mov [rel crc_poly], rax			; Zapisujemy wielomian crc	


	; Otwieramy plik
	mov rax, SYS_OPEN
	mov rdi, r10					   ; Przekazujemy ścieżkę pliku
	mov rdx, READ_ONLY
	mov rsi, 0
	syscall
	test rax, rax
	js .open_failed
	mov [rel file_descriptor], rax; Zapisujemy file descriptor

		
	; Otwarcie powiodło się
	mov r12, 1						   ; r12 będzie trzymał zapolny bit o crc_length-1
	mov cl, byte [rel crc_poly_len]
	dec cl
	shl r12, cl;
	
	xor r9, r9 						   ; Zapiszemy tutaj wynik crc_poly
.fragment_loop:
	mov rax, SYS_READ				   ;
	mov rdi, [rel file_descriptor];
	mov rsi, block_length			;
	mov rdx, 2						   ;
	syscall
	cmp rax, 2
	jb .read_failed

   ; Wczytujemy buffor
   ;xor r11, r11
   mov rsi, buffer					;
   mov rdi, [rel file_descriptor];
   movzx rdx, word [rel block_length]		
.not_all_read_again:
   mov rax, SYS_READ				   ;	
	syscall
	test rax, rax
	js .read_failed
   add rsi, rax
   neg rax
   add rdx, rax                  ;
   test rdx, rdx
   jnz .not_all_read_again
	; W tym momencie mamy juz porządany fragment w bufforze
	; Wynik crc poly będziemy trzymać w r9
	; liczbę bajtów już wykorzystanych trzymać będzimy w rcx
	movzx rdx, word [rel block_length]
	test rdx, rdx
	jz .finished_fragment_crc


	xor r11, r11
.block_loop:
	mov bl, byte [rel buffer + r11]; zapis kolejnego bajtu do odczytu
	mov al, 0x80					   ; bit mapa rozważaniego bitu
.bajt_loop:
	xor cl, cl
	test r9,r12
	jnz .xor_needed
	inc cl
.xor_needed:
	shl r9, 1
	test bl, al						; Czytamy kolejny bajt
	jz .new_bit_is_zero
	inc r9
.new_bit_is_zero:
	test cl,cl
	jnz .bit_is_0					; Bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [rel crc_poly]
.bit_is_0:

	shr al, 1
	test al,al 
	jnz .bajt_loop

	inc r11
	cmp r11, rdx
	jne .block_loop
.finished_fragment_crc:

	mov rax, SYS_READ				;
	mov rdi, [rel file_descriptor]	   	
	mov rsi, shift					; Zapisujemy 4 bajtowy fragment skoku
	mov rdx, 4			         ;
	syscall
   cmp rax, 4
	jb .read_failed


   mov rax, SYS_LSEEK         ; Syscall number for lseek
   mov rdi, [rel file_descriptor]
   movsx rsi, dword [rel shift]

   mov rdx, SEEK_CUR          ; SEEK_CUR flag
   syscall

   test rax, rax
   js .seek_failed

   mov eax, dword [rel shift];
   test eax,eax
   jns .fragment_loop
   neg eax
   movzx edx, word [rel block_length];
   add edx, 6
	cmp eax, edx
	jne .fragment_loop

	mov rax, 1;
	mov cl, [rel crc_poly_len]
	
.zero_loop:
	xor al, al						; Wszystko jedno jaki wynik wcześniejszych operacji przesuwamy w lewo i dodajemy nowy bit z buffera
	test r9,r12
	jnz .xor_needed_zero_loop
	inc al
.xor_needed_zero_loop:
	shl r9, 1
	dec cl
	test al,al
	jnz .bit_is_0_2			   ; Bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [rel crc_poly]
.bit_is_0_2:
	test cl,cl	
	jnz .zero_loop


 mov rcx, crc_poly_out + 64 	; Pointer na koniec fragmentu
   mov byte [rcx], `\n` 		; Wpisujemy znak konca linii
   dec rcx						
	mov rbx, r9
.convert_to_binary:
   mov rdx, rbx
   and rdx, 1 					   ; Bierzemy najbardziej znaczący bit
   add dl, '0' 				   ; Zmieniamy na ASCII
   mov [rcx], dl 				   ; Zapisuemy 
   dec rcx
   shr rbx, 1
   cmp rcx, crc_poly_out
   jge .convert_to_binary 

 	mov rax, SYS_WRITE 
   mov rdi, STDOUT
	movzx rcx, byte [rel crc_poly_len]
	add rcx, -64
	neg rcx                    ; rcx = 64 - crc_poly_len
	movzx rdx, byte [rel crc_poly_len]
	inc rdx                    ;
   lea rsi, [crc_poly_out + rcx] 
   syscall
   test rax, rax
	js .write_failed

	mov rax, SYS_CLOSE
	mov rdi, [rel file_descriptor]
	syscall
	test rax, rax
	js .close_failed
   ; syscall exit(0)
	xor edi, edi
	mov eax, SYS_EXIT
	syscall

.close_failed:
.seek_failed:
.read_failed:
.write_failed:
	mov rax, SYS_CLOSE
	mov rdi, [rel file_descriptor]
	syscall

.open_failed:
.input_crc_poly_error:
_error:
	mov eax, SYS_EXIT
	mov edi, 1
	syscall
