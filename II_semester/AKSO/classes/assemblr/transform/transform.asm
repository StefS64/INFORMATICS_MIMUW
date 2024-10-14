global transform
extern putchar

section .text






;
;
;
;





transform:
; Cały czas trzymamy wskaźnik s na wierzchołku stosu chil pod rsp
; (bo wywyołanie putchar nadpiszą rdi
	push rdi

	mov al, [rdi]; Czytaj pierwszy znak ASCII zostaje zapisany w rejestrze al
	cmp al, '+' ; sprawdza czy plus
	je .sum ; jump if equal (wiadomo gdzie) do .sum



; Sprawdzamy tutaj czy coś jest literą jeżeli nie jest to returnujemy
	cmp al, 'a'
	jb .return; mniejsze  
	cmp al, 'z'
	ja .return; wieksze 

;wypisz al;
	movzx rdi, al; musimy zmienić typ do większego i wyzerować wyższe bity dlatego movzx
	call putchar wrt ..plt
	inc qword [rsp]

.return:
	pop rax
	ret


.sum:
	mov rdi, '('
	call putchar wrt ..plt

	mov rdi, [rsp] ; wskazuje na plusa a nie na początek wyrażenia więc dlatego inc
	inc rdi
	call transform
	mov [rsp], rax
	
	mov rdi, '+'
	call putchar wrt ..plt
	

	mov rdi, [rsp]
	call transform
	mov [rsp], rax


	mov rdi, ')' 
	call putchar wrt ..plt
	jmp .return






