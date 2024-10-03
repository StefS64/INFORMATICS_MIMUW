global _start

;%include "macro_print.asm"
;.data



section .bss

buffer resb 64000				; Fragment na którym liczymy crc(bez dziur)
block_length resb 2				; Dlugosc fragmentu (bez dziur)
shift resb 4					; Dlugosc skoku po fragmencie
crc_poly resq 1					; Wielomian
crc_poly_len resb 1				; Dlugosc wielomianu
file_descriptor resq 1			;	
crc_poly_out resb 65			; Zapis wyniku w stringu



section .text
; wywołania systemowe
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
	mov rcx, [rsp]				; Wkłada do rcx liczbę argumentów
	cmp rcx, 3					; Sprawdza czy liczba argumentów jest poprawna
	;	print "liczba_argumentow = ", rcx
	jne _error
	mov r10 , [rsp + 16]		; wczytujemy sciezke pliku
	mov rdi, [rsp + 24] 		; zapisuje adres cyrc_poly
	
.parse_string_poly_crc:			; Parsuje podanego stringa i zapisuje go w rax
	movzx rbx, byte [rdi];
	cmp bl, 0		;
	je .poly_crc_written
	sub bl, '0';
	cmp bl, 1
	ja .input_crc_poly_error	; Sprawdza poprawnosc    0 <= bl <= 1
	shl rax, 1;
	or al, bl;
	inc rdi
	inc byte [crc_poly_len]		; Zapisuje długosc wielomianu
	jmp .parse_string_poly_crc
.poly_crc_written:
	;	print "input = ", rax	;
	mov [crc_poly], rax			; Zapisujemy wielomian crc	
	;	print "crc_poly_length =", word [crc_poly_len]


	;Otwieramy plik
	mov rax, SYS_OPEN
	mov rdi, r10				; Przekazujemy ścieżkę pliku
	mov rdx, READ_ONLY
	mov rsi, 0
	syscall
	test rax, rax
	js .open_failed
	mov [file_descriptor], rax		; Zapisujemy file descriptor

		
	;Otwarcie powiodło się
	xor r9, r9 ;zapiszemy tutaj wynik crc_poly
.fragment_loop:
	mov rax, SYS_READ					;
	mov rdi, [file_descriptor]			;
	mov rsi, block_length				;
	mov rdx, 2							;
	syscall
	test rax, rax
	js .read_failed

	
   ;debug
	;   movzx rax, word [block_length]
	;	print "fragment length = ", rax;
   ;end debug

   ;Wczytujemy buffor
   mov rax, SYS_READ						;
	mov rdi, [file_descriptor]				;
	mov rsi, buffer							;
	movzx rdx, word [block_length]			;
	syscall

	test rax, rax
	js .read_failed

   ;debug
    ;	movzx rax, byte [buffer]
	;	print "dany fragment = ", rax;
	;end debug
	

	; W tym momencie mamy juz porządany fragment w bufforze
	; Wynik crc poly będziemy trzymać w r9
	; liczbę bajtów już wykorzystanych trzymać będzimy w rcx
	movzx rdx, word [block_length]
	test rdx, rdx
	jz .finished_fragment_crc
	mov r12, 1						; r12 będzie trzymał zapolny bit o crc_length można to zadeklarować wcześniej
	mov cl, byte [crc_poly_len]
	shl r12, cl;

	xor r11, r11
.block_loop:
	;debug
	;	print "bajt_num = ", r11;
	;end debug
	mov bl, byte [buffer + r11]		; zapis kolejnego bajtu do odczytu
	mov al, 0x80					; bit mapa rozważaniego bitu
.bajt_loop:
   	;debug
	;	print "obliczony crc fragmentu przed xor = ", r9;
;	;end debug
	test r9,r12
	jz .bit_is_0	;bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [crc_poly]
	xor r9, r12
.bit_is_0:					; wszystko jedno jaki wynik wcześniejszych operacji przesuwamy w lewo i dodajemy nowy bit z buffera
	shl r9, 1
	test bl, al				; czytamy kolejny bajt
	jz .new_bit_is_zero
	xor r9, 1
.new_bit_is_zero:
   ;debug
	;	print "obliczony crc fragmentu = ", r9;
	;end debug

	shr al, 1
	test al,al 
	jnz .bajt_loop


	inc r11
	cmp r11, rdx
	jne .block_loop

.finished_fragment_crc:

	mov rax, SYS_READ					;
	mov rdi, [file_descriptor]	   ; 
	mov rsi, shift						; Zapisujemy 4 bajtowy fragment skoku
	mov rdx, 4			            ;
	syscall

   test rax, rax
	js .read_failed


   mov rax, SYS_LSEEK             ; syscall number for lseek
   mov rdi, [file_descriptor]
   movsx rsi, dword [shift]
   ;debug
	;	print "real_shift = ", rsi;
	;end debug
   mov rdx, SEEK_CUR             ; SEEK_CUR flag
   syscall

   test rax, rax
   js .seek_failed

   	;debug
	;  mov eax, dword [shift]
	;	print "shift = ", rax;
	;end debug

   mov eax, dword [shift];

   test eax,eax
   jns .fragment_loop
    ;  print "shift = ", rax;
   neg eax
   ;debug
	;	print "neg_shift = ", rax;
	;end debug
   movzx edx, word [block_length];
   add edx, 6
   ;debug
	;	print "block + 6 = ", rdx;
	;end debug
   cmp eax, edx
   jne .fragment_loop

	mov rax, 1;
	mov cl, [crc_poly_len]
	
.zero_loop:
	test r9,r12
	jz .bit_is_0_2	;bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [crc_poly]
	xor r9, r12
.bit_is_0_2:					; wszystko jedno jaki wynik wcześniejszych operacji przesuwamy w lewo i dodajemy nowy bit z buffera
	shl r9, 1
	dec cl
	jnz .zero_loop

	test r9,r12
	jz .bit_is_0_3	;bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [crc_poly]
	xor r9, r12
.bit_is_0_3:
   ;debug
	;	print "koncowy wynik crc = ", r9;
	;end debug

 mov rcx, crc_poly_out + 63 ; Point to the end of bin_rep
    mov byte [rcx], `\n` ; Null terminator
    dec rcx
	mov rbx, r9
.convert_to_binary:
    mov rdx, rbx
    and rdx, 1 ; Isolate the least significant bit
    add dl, '0' ; Convert to ASCII '0' or '1'
    mov [rcx], dl ; Store in bin_rep
    dec rcx
    shr rbx, 1 ; Shift right
    cmp rcx, crc_poly_out
    jge .convert_to_binary ; Repeat 64 times

 	mov rax, SYS_WRITE ; syscall number for sys_write
    mov rdi, STDOUT ; file descriptor 1 is stdout
	movzx rcx, byte [crc_poly_len]
	;debug
	;	print "wypisanie = ", rcx;
	;end debug

	add rcx, -63
	;debug
	;	print "wypisanie = ", rcx;
	;end debug
	neg rcx
	;debug
	;	print "wypisanie = ", rcx;
	;end debug
	movzx rdx, byte [crc_poly_len]
	inc rdx
    lea rsi, [crc_poly_out + rcx] 
    syscall

	; close file
	mov rax, SYS_CLOSE
	mov rdi, [file_descriptor]
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
	mov rax, SYS_CLOSE
	mov rdi, [file_descriptor]
	syscall

.open_failed:
.input_crc_poly_error:
_error:
	mov eax, SYS_EXIT
	mov edi, 1
	syscall

