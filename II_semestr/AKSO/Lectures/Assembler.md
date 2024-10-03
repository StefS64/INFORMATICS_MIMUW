#assembler

movzs

Parowanie rejestrów aby mieć więcej miejsca 




## lea

lea rbx, [rdi+2*rdx] - do rbx jest ładowany efektywny adres tego w bramce;rbx = rdi+2*rdx ( niestandardowo to nie jest odwołanie do pomięci )


[] - odwołanie do pamięci


### Rozkac y arytmetyczne 
add <dest> <src> 
nie można dodoać bezpośrednio do komórek jedynie musi to przejść przez rejestr
Można wpisywać wartości mniejsze funkcje te automatycznie rozszerzają elementy
sub




####adc sbb
<dest> = <dest +/- (<src> + CF)



### inc dec - ++ --

uwaga nie zmienia znacznika przeniesienia

przykłady
inc rax
dec ax
inc dword [variable] - tutaj musi być dword (nie wiadomo jak wielkie jest to co chcesz zwiększyć potrzebna jest wielkość danego słowa)
- rozkazy muszą implicite określać rozmiar danej operacji

### Mnożenie

mul - operacja jednoargumentowa Mnoży bez znaku
- domnaża wartość do akumulatora
zapiasywan e są w dwóch elemntach ze względu na historyczną na dwa


imul - mnożenie z znakiem
3 warianty 
1 argumentowy taki jak mul

2 argumentowy <reg> <op/imm> - elementy muszą być takiego samego rozmiaru potem obcina to co się przepełni

3 argumentowy <reg, <op>, <imm>

### dzielenie
 div 

### operacje logiczne na bitach
and	and rbx rbx - sprawdzamy czy zero
or
xor - xor eax eax - wyzeruje eax
not - zanegowanie liczby

operacje te powodują wyznaczeni znaczników 

### przesunięcia
shr 
najmniej znaczący bit przesuwa się do CFa 
Na początek zawsze wchodzi zero
sar - powiela ostatni bit

sal - tak samo jak shr - przesunięcie arytmetyczne w lewo

ror - bit który spadł wchodzi na miejsce które jest wolne z przodu.








## Sterowanie przebiegu programu
- korzystamy z skoków
Modyfikuje rejestr RIP
RIP - instruction pointer zawiera adres nastpednego rozkazu do pobranie z pamieci.

Jump jako argument bierze ten adres do którego chce skoczyć 

Każdy rozkaz który piszemy może być poprzedzony etykietą przy odwołaniu jumpem mpiszemy etykietkę:

pocz: xor ax ax
jmp pocz (skok bezwarunkowy)


- może być względny lub bezwzględny



### Skok warunkowy
jak coś jest spełnione to będzie git

przykład:

petla: mov rax 8
dec rax - rozkazy tutaj nie modyfikują rax
jnz pętla

^for(int i = 0 i != 0; i--)



### ustawanie znaczników

test rax 0 - zrobi and rax 0 



cmp rax 15


- oba te operacje nie zmieniają składnika docelowego



### WIECEJ SKOKÓW NA SLAJDACH
JS
JNS
JL JNGE


###Pryzkłady
-procesor nie interpretuje bitów w U2 lub NKB o tym decyduje programista
- za kązdym razem są ustawiane SF CF i OF 
overflow




### STOS
push - doda 8 bajtów na stosie
pop - usunie 8 bajtów na stosie.



### Rozkazy warunkowe

- dwuetapowe
SET - przypisanie warunkowe
CMOV - przepisanie warunkowe
Trzeba odpowiednio dobrać jumpa by liczby było odpowiedznio iterpretowane




