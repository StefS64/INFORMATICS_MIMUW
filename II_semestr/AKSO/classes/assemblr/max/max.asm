global smax, umax
;
;
;
smax:
	cmp edi, esi
	cmovg eax, edi
	cmovng eax, esi
ret


umax:
	cmp edi, esi
	mov eax, esi
	cmovnb eax, edi
ret
