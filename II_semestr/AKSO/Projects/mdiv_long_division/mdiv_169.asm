global mdiv

section .text
; rdi - x wskaźnik na tablicę liczb int64 nasza dzielna
; rsi - n długość tej tablicy
; rdx - y dzielnik
mdiv:	
	mov r8, rdx 						; Będzimy wykorzystywać rejestr rdx zatem zapisujemy wartość y w rejestrze r8 
	xor r11b, r11b

	mov rax, 0x8000000000000000
	mov rcx, rsi
	cmp r8, -1
	jne .continue
	dec rcx
	cmp qword [rdi+ rcx*8], rax
	jne .continue
.check:																
	cmp qword [rdi + (rcx-1)*8], 0
	jne .continue
	loop .check	
	xor rcx,rcx
	div rcx
.continue:
;to wywalamy ^ sparwdzawmy po abs będzie ponownie 0x8000... jednyna liczb ujemna przy absolute i sprawdzamy też -1 które jest zapisany r11b jako 1 jeżeli ujemna czyli sprawdzić czy 1 nie używamy rdx( nadal jest dzielnik więc można to wykorzystać po prostu rdx 
	test r8, r8
	jns .positive_y
	add r11b, 1;
	neg r8
.positive_y:											;W tym momencie programu y jest zawsze liczbą  dodatnią jeżeli nastąpiła zmiana znaku 1bit r11b został ustawiony na 1
	mov rax, [rdi + (rsi - 1)*8]					
	test rax, rax										; sprawdzamy czy liczba jest ujemna 						
 	jns .positive_x													
	add r11b, 2

	mov rcx, rsi										
	xor r10, r10					
	stc																														
.abs_x_for:												; funkcja przechodzi przez tablicę x od najmniej znaczących bitów negując liczbę i dodając jeden przy przepełnieniu									
	not qword [rdi + r10*8]	
	adc qword [rdi + r10*8], 0 					; dodajemy jeden przy przepełnieniu
	inc r10						
	loop .abs_x_for									; loop nie zmienia Cary flag zatem add carry zadziała normalnie

.positive_x:											;W tym momencie programu x jest zawsze dodatni chyba że był najmniejszą możliwą liczbą 0x800000... wtedy po wartości bezwględnej nadal ma tą samą wartość
;tutaj sprawdzamy czy jest prypadek overflowu
xor rdx, rdx
mov rcx, rsi					
.div:											; do 
	mov rax, [rdi + (rcx-1)*8]			; rax = x[rsi]
	div r8									; rax =  rdx * 2^64 + x[rsi] / r8
	mov [rdi + (rcx-1)*8], rax 		; x[rsi] = rax
	loop .div								; while(rcx > 0)
	mov rax, rdx


	cmp r11b, 3 				; odzyskujemy odpowiednie informacje o znaku oraz ustawiamy znaki w taki sam sposób jak idiv
	je .bl3

	cmp r11b, 
	je .bl2 2

	cmp r11b, 1
	je .bl1
ret
.bl3:				; x oraz y orginalnie były ujemne
	neg rax
ret
.bl2:				; x orginalnie było ujemne y dodatnie 
	neg rax
.bl1:				; y orginalnie było ujeme x dodatnie
	mov rcx, rsi
	xor r10, r10
	stc
.abs_x_for2:	
	not qword [rdi + r10*8]						
	adc qword [rdi + r10*8], 0 
	inc r10			
	loop .abs_x_for2	
ret
