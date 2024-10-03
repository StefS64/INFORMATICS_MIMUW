global mdiv

section .text
; rdi - x wskaźnik na tablicę liczb int64 nasza dzielna
; rsi - n długość tablicy
; rdx - y dzielnik
; Wartości rejestróe rsi oraz rdi pozostają bez zmian do zakończenia programu
; Dla ułatwienia liczbę n*64 bitową zapisaną jako x[] będziemy nazywać w komentarzach jako X
mdiv:	
	mov r8, rdx 						;<- zapisujemy y w r8, r8 nie będzie zmieniany do końca programu.
	xor r11b, r11b						;<- w r11b na 1 bicie trzymamy znak y a na drugim (bardziej znaczącym) znak X, oraz 
											;	na  3 bicie trzymamy czy pętla .negation została już wykorzystana po raz pierwszy.

; Ten blok programu zapisuje odpowiednie znaki liczb X oraz y w r11b, oraz
; zmienia wartości tych liczb na ich wartości bezwzględne. 
; UWAGA w przypadku X  = 0x8000000... (najmniejszej możliwej liczbie)  |X| = X (negacja bloków oraz dodanie jedynki jej nie zmienia)
; Koniec bloku jest sygnalizowany etykietą .positive_X
	test r8, r8								
	jns .positive_y
	inc r11b;							;<- zapisujemy znak jednykę możemy zapisać jako dodanie 1
	neg r8
.positive_y:												
	mov rax, [rdi + (rsi - 1)*8]	;<- zapisujemy w rax	blok zawierający znak liczby X			

	test rax, rax																
 	jns .positive_X										
	or r11b, 2							;<- zapisujemy znak

	; Pętla przechodzi przez tablicę x[i] od najmniej znaczących bitów negując liczbę x[i] dodając jeden przy przepełnieniu.
	; Jako "i" służy nam rejestr r10, loop jest wykorzystywany aby nie zmienić wartości CF.
	; Po zakończeniu pętli usyskamy |X| chyba, że X =0x8000... wtedy X pozostanie bez zmian.			
.negation:
	mov rcx, rsi						;<- rcx = n
	xor r10, r10
	stc									;<- dodajemy jeden poprzez ustawienie CF = 1	
.abs_X_for:							
	not qword [rdi + r10*8]	
	adc qword [rdi + r10*8], 0
	inc r10						
	loop .abs_X_for
	test r11b, 4						;<- w przypadku drugiego obiegu program się kończy wszystkie liczby zostały poprawnie ustawione.
	jnz .end
	; Koniec pętli.

	;Ten blok kodu aż do labela ".continue" sprawdza czy nastąpiłoby przepełnienie, jedynym takim przypadkiem jest gdy: X = 0x8000... i y = -1;
	cmp rdx, -1
	jne .continue
	and rdx, [rdi + (rsi - 1)*8]	;<- 0x800... jest jedyną liczbą ujemną zatem sprawdzamy znak poprzez "and" z -1 ( pozostaje sprawdzić bit znaku ).
	jns .continue
	div cl								;<- zgłoszenie przerwania poprzez dzielenie przez 0 rejest rcx jest wyzerwoany dzięki pętli .abs_X_for, która się kończy gdy rcx = 0.
.continue:
.positive_X:



; Blok implementujący dzielenie liczb dodatnich przechodzi przez liczbę X zapisaną w tablicy x[]. 
; Dzieli ją 128 bitowo, dzieląc parę rejestrów rdx:rax przez y(r8) reszta z dzielenia tego fragmentu jest zapisywana w rdx.
; Zatem wystarczy dopisać kolejny fragment do rejestru rax.
; W przypadku gdy y = 0 w tym momencie program podzieli przez zero pośrednio zgłaszającdzielenia przez zero nastąpi przerwanie obsłużone za pomocą funkcji div.
	xor rdx, rdx
	mov rcx, rsi					
.div: 
	mov rax, [rdi + (rcx - 1)*8]	
	div r8								;<-dzielimy parę rejestrów rdx:rax reszta zapisuje sie rdx wynik w rax.
	mov [rdi + (rcx - 1)*8], rax 	;<-zapisujemy wynik z dzielenia.
	loop .div							
	mov rax, rdx						;<-ostateczna reszta z dzielenia.



; Ostatni blok kodu polegający na odzyskaniu znaku liczby zgodnie z specyfikacją.
	cmp r11b, 2							;<- jeżeli liczba X była ujemna.
	jnb .X_was_negative

	cmp r11b, 0							;<- X oraz y były dodatnie.
	je .end

.y_was_positive:						;<- wywoła się jedynie gdy albo X ujemne i y dodatnie lub gdy X dodatnie i y ujemne.
	xor r11b, 4							;<- ustawiamy 3 bit r11b na 1 informujący o numerze wykorzystania nagacji.  
	jmp .negation						;<- trzeba zmienić znak liczby X zatem wykorzystujemy wcześniej napisaną negację.
.end:
ret

.X_was_negative:
	neg rax
	cmp r11b, 2							;<- y było dodatnie
	je .y_was_positive
ret
