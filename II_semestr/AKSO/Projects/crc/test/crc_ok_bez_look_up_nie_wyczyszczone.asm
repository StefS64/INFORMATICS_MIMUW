global _start

;%include "macro_print.asm"



section .bss

buffer resb 66000					; Fragment na którym liczymy crc(bez dziur)
block_length resb 2					; Dlugosc fragmentu (bez dziur)
shift resb 4						; Dlugosc skoku po fragmencie
crc_poly resq 1						; Wielomian
crc_poly_len resb 1					; Dlugosc wielomianu
file_descriptor resq 1				;	
crc_poly_out resb 65				; Zapis wyniku w stringu

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
	cmp rcx, 3						; Sprawdza czy liczba argumentów jest poprawna
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
	inc byte [rel crc_poly_len]			; Zapisuje długosc wielomianu
	jmp .parse_string_poly_crc
.poly_crc_written:

	mov [rel crc_poly], rax				; Zapisujemy wielomian crc	


	; O twieramy plik
	mov rax, SYS_OPEN
	mov rdi, r10					; Przekazujemy ścieżkę pliku
	mov rdx, READ_ONLY
	mov rsi, 0
	syscall
	test rax, rax
	js .open_failed
	mov [rel file_descriptor], rax		; Zapisujemy file descriptor

		
	;Otwarcie powiodło się
	mov r12, 1						; r12 będzie trzymał zapolny bit o crc_length-1
	mov cl, byte [rel crc_poly_len]
	dec cl
	shl r12, cl;
	
	xor r9, r9 						; Zapiszemy tutaj wynik crc_poly
.fragment_loop:
	mov rax, SYS_READ				;
	mov rdi, [rel file_descriptor]		;
	mov rsi, block_length			;
	mov rdx, 2						;
	syscall

	test rax, rax
	js .read_failed	
   ;debug
	;   movzx rax, word [block_length]
	;	print "fragment length = ", rax;
   ;end debug

   ; Wczytujemy buffor
   	mov rax, SYS_READ						;
	mov rdi, [rel file_descriptor]				;
	mov rsi, buffer							;
	movzx rdx, word [rel block_length]			;
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
	movzx rdx, word [rel block_length]
	test rdx, rdx
	jz .finished_fragment_crc


	xor r11, r11
.block_loop:
	;debug
	;	print "bajt_num = ", r11;
	;end debug
	mov bl, byte [rel buffer + r11]		; zapis kolejnego bajtu do odczytu
	mov al, 0x80					; bit mapa rozważaniego bitu
.bajt_loop:
   	;debug
	;	print "obliczony crc fragmentu przed xor = ", r9;
;	;end debug
	xor cl, cl
	test r9,r12
	jnz .xor_needed
	inc cl
.xor_needed:
	shl r9, 1
	test bl, al						; Czytamy kolejny bajt
	jz .new_bit_is_zero
	xor r9, 1 ; tu można inc
   ;debug
	;	print "dodano dziada = ", r9;
;	;end debug
.new_bit_is_zero:
    ;debug
	;	print "przesunieto fragment = ", r9;
;	;end debug
	test cl,cl
	jnz .bit_is_0					; Bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [rel crc_poly]
   ;shl r12,1;temp
	;xor r9, r12 ;to usunac przy żyłowaniu
   ;shr r12,1;temp
   ;debug
	;	print "dokonano xora fragmentu = ", r9;
;	;end debug
.bit_is_0:
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

	mov rax, SYS_READ				;
	mov rdi, [rel file_descriptor]	   	; 
	mov rsi, shift					; Zapisujemy 4 bajtowy fragment skoku
	mov rdx, 4			            ;
	syscall
	;debug
	;	print "read ", rsi;
	;end debug
   	test rax, rax
	js .read_failed


   mov rax, SYS_LSEEK             	; Syscall number for lseek
   mov rdi, [rel file_descriptor]
   movsx rsi, dword [rel shift]
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

   mov eax, dword [rel shift];

   test eax,eax
   jns .fragment_loop
    ;  print "shift = ", rax;
   neg eax
   ;debug
	;	print "neg_shift = ", rax;
	;end debug
   movzx edx, word [rel block_length];
   add edx, 6
   ;debug
	;	print "block + 6 = ", rdx;
	;end debug
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
	jnz .bit_is_0_2					; Bit interesujący nas jedynką xorujemy wynik.
	xor r9, qword [rel crc_poly]
.bit_is_0_2:
	test cl,cl	
	jnz .zero_loop






   ;debug
	;	print "koncowy wynik crc = ", r9;
	;end debug

 mov rcx, crc_poly_out + 64 	; Pointer na koniec fragmentu
    mov byte [rcx], `\n` 		; Wpisujemy znak konca linii
    dec rcx						
	mov rbx, r9
.convert_to_binary:
    mov rdx, rbx
    and rdx, 1 					; Isolate the least significant bit
    add dl, '0' 				; Convert to ASCII '0' or '1'
    mov [rcx], dl 				; Zapisujemy 
    dec rcx
    shr rbx, 1
    cmp rcx, crc_poly_out
    jge .convert_to_binary 		; Repeat 64 times

 	mov rax, SYS_WRITE 			; Syscall number for sys_write
    mov rdi, STDOUT 			; File descriptor 1 is stdout
	movzx rcx, byte [rel crc_poly_len]
	;debug
	;	print "wypisanie = ", rcx;
	;end debug

	add rcx, -64
	;debug
	;	print "wypisanie = ", rcx;
	;end debug
	neg rcx
	;debug
	;	print "wypisanie = ", rcx;
	;end debug
	movzx rdx, byte [rel crc_poly_len]
	inc rdx
    lea rsi, [crc_poly_out + rcx] 
    syscall

	; close file
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
	mov rax, SYS_CLOSE
	mov rdi, [rel file_descriptor]
	syscall

.open_failed:
.input_crc_poly_error:
_error:
	mov eax, SYS_EXIT
	mov edi, 1
	syscall