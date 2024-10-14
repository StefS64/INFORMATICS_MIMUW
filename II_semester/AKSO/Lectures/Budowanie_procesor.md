#Procesor



Pierwsza faza w pierwszym cyklu zegara, faza FETCH

(przechodzi przez pamięć i odczytuje wartość pod danym adresem)

"Sygnał" jest zatrzymywany na następnych bramkach które są uaktywanione zmianą syklu zegara na "on"





Inna Faza Memeory acces odczyt pamięci z jakiejś funkcji





# Strategie zarządzania


## Fragmentacja wewnętrzna

 - część obszaru otrzymanego przez obaszar jest niewykorzystywane 
- słabo	


## Przydział ciągły pamięci
 - powstają dziury
 - które warto usupełnić przy przydzialniu pamięci na procesy
 - procesy nie muszą sie kończyć w tym samym czasie zatem proces powstawanie dziur się powtarza 
### Przy pojawieniu się nowego proces umusimy mu przydzialic pamiec wpasować go do dziury parę strategii:
	- Best fit
	- worst fit ( zostanie dostatecznie duża dziura by pozostawić na inne procesy) 
	- first fit
Które są lepsze:
	- first fit najszybsze 
	- worst fit jednak gorszy od reszty best fit i first porównywalne pod względem pamięci
## Fragmentacja Zewnętrzna
	- marnuję się pamięć na zewnątrz procesu
	- suma rozmiarów jest git do zrobienia procesu, ale  "dziury" są za małe
### Jak przeciwdziałać?
	- Defragmentacja
		-	wymaga przesuawania programu w czasie jego wykonywania ( trochę słabo i drogie)
		- możliwe jedynie wtedy gdy jest dynamicznie alokowana
	- Zrobienie tak by procesy nie musiały mieć wspólnego podciągu.





# Stronnicowanie (będzie na egzaminie)

- stosowany w większości wspołczesnych architectur
- rezygnujemy z tego że proces ma pamięc w jednym wspólnym kawałku.
## Jak to uzyskać?
	- Pamięc fizyczna jest dzielona na ramki o takim samym rozmiarze będące potęgą dwójki,
	- Pamięc logiczna jest podzieloana na strony o takim samym rozmiarze co ramka.
Logicznie Strony są wprowadzane w odpowiednie ramki.
Jest tablica która indeksuje odpowiednie ramki (TABLIC STRON)

każdy proces ma własną tablicę stron.


!!!!! Stronicowanie wymga wsparcia sprzętowego.

Sprzęt zmienia adres logiczny na adres fizyczny przez SPRZĘT!!!!!



##Adres logiczny
- ciąg bitów 
logiczny podział na:
ofset i numer strony.

### Translacja adresu
 SPRZĘT bierze te ciągi bitów i wstawia odpowiednie warto

Inna Implemntacja tablicy stron:

Sprzęt ma specjalny rejest zawierający adres PTBR


## Problemy z stronnicowaniem 

 - Tablica stron może być bardzo duża



- Jednal nie zwalnia bo jest Translation Look- asiade Buffor

