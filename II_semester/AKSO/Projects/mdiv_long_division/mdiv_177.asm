global mdiv

section .text
; rdi - x wskaźnik na tablicę liczb int64 nasza dzielna
; rsi - n długość tej tablicy
; rdx - y dzielnik
mdiv:	
	mov r8, rdx 						; y zapisywanie 
	mov r9, rsi							; n
	xor r11b, r11b

	mov rax, 0x8000000000000000
	mov rcx, r9
	cmp r8, -1
	jne .continue
	dec rcx
	cmp qword [rdi+ rcx *8], rax
	jne .continue
.check:																
	cmp qword [rdi + (rcx-1)*8], 0
	jne .continue
	loop .check	
	xor rcx,rcx
	div rcx
.continue:
	test r8, r8
	jns .positive_y
	add r11b, 1;
	neg r8
.positive_y:			; komentarze jeszce
	mov rax, [rdi + (rsi - 1)*8]
	test rax, rax
 	jns .positive_x									;liczba jest ujemna				
	add r11b, 2
	mov rcx, r9
	xor r10, r10					
	stc																																				
.abs_x_for:																
	not qword [rdi + r10*8]	
	adc qword [rdi + r10*8], 0 			; x[rcx] = rax
	inc r10								; - rcx jest zawsze większe od zera zatem CF pozostanie bez zmian
	loop .abs_x_for	

.positive_x:

xor rdx, rdx
mov rcx, r9
.div:								; do 
	dec rcx			;tu może na loop zmienić				; rsi--
	mov rax, [rdi + rcx*8]			; rax = x[rsi]
	div r8							; rax =  rdx * 2^64 + x[rsi] / r8
	mov [rdi + rcx*8], rax 			; x[rsi] = rax
	cmp rcx, 0						; while( rsi != 0)
	jne .div
	mov rax, rdx
	cmp r11b, 3
	je .bl3

	cmp r11b, 2
	je .bl2

	cmp r11b, 1
	je .bl1

ret
.bl3:
	neg rax
	ret
.bl2:
	neg rax
.bl1:
	mov rcx, r9
	xor r10, r10
	stc
.abs_x_for2:	
	not qword [rdi + r10*8]						
	adc qword [rdi + r10*8], 0 			; x[rcx] = rax
	inc r10						; - rcx jest zawsze większe od zera zatem CF pozostanie bez zmian
	loop .abs_x_for2	
ret