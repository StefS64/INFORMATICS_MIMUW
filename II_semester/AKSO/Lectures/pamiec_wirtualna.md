# Wsclock

stara się zachować w pamieci te strony które są potrzebne.

Ma stałą T która oznacza ilość jednostek czasu po której usuwa stronę 
Po usunieciu szuka dalej.
Jeżeli strona była zabrudzona to nie może być usunitęta( nie zmieniła się od ostatniego zapisu na dysk)
Jeżeli nie ma trzego usunąć czyli wszystkie są wykorzystywane, może się zdarzyć migotanie stron.


# Inne wykorzystanie pamięci wirtualnej

Przydaję się przy wykorzystaniu funkcji fork()
Jest to prawie kopia wykonywanego procesu.

## Copy On Write(Kopiowanie przy zapisie)
	- początkowo proces potomny i macierzysty współdzielą te same strony
	- System operacyjny oznacza stronki do odczytu
tablica stron					tablica stron nowego procesu
[]									[]
[x = 3]-\					  /[x = 3]
[]			|					/	[]
[]			|				  /	[]
[] 		|			__       []
			|
		  \//
RxM	[][x = 3][][][]	

przy braku zmiany zmiennej nie zachodzi kopiowanie w stronie(wszytsko dzieje sie na stronach ) - upewnic sie

## Odwzorowanie pliku na pamięć
Bierzemy sysk twardy który jest podzielony na bloki długości strony 4kb(dla uproszczenia)
- proces otwiera plik
wpisuje do tablicy stron aby pewien zakres pamięci wirtualnej prowadził do fragmentów na dysku
Zatem przejmuje teraz system operacyjny pałeczkę.
SO zleca układowi DMA przyjęcie strony z dysku do RAM.
bit poprawności odwołania zostanie zapisany na tablicy stron.
na tablicy stron odwołuję sie do RAM
jeżeli już nie ma miejsca to pojawi się błąd braku strony.


- Jeszcze brakuje nam miejsca gdzie dana strona jest składowana na dysku( wiec to gdzies jeszcze jest zapisane)
- co ciekawe nie ma tego w tablicy stron(nie ma tam miejsca)



Jest specjalna funkcja do mmapowania MMAP.



# Pamięć wirtualna w Linuxie
## Węzły
	Są po to aby zapewnić sparcie dal architektur nie jednorodnych ( mamy kilka różnych banków pamięci, pamieci RAM różnych poziomów NUMA)
	w UMA nie ma tego zjawiska ( nie ma różnych rodzajów RAM na PCcie zwykle jest UMA)
	Zatem węzeł odpowiada jednymu blokowi pamięci 
	Linux przechowyje listę tych węzłów
Węzeł jest podzielony na strefy
___Uwaga rozmiawiamy o pamieci rzeczywistej___
## Strefy
w  32 bit
- ZONE_DMA
- ZONE_NORMAL
- ZONE_HIGHMEN
## Ramki
 - służy do zapamiętania informacji o stron 
reprezentowane przez struct page
przechowywane w globalnej tablicy ramek mem_map.
## Strony
- wielopoziomowa
PGD - najbardziej zewnętrzna tablica stron. GLobal 
zajmuję jedną ramkę, każda aktywwna pozycja wskazuje na Page middle directory
Obszary
Każdy element to 4B który opisuje PMD
A PMD prowadzi do PTE która trzyma numery ramki 
Jak widać mamy schemat 3 poziomowy
___ Uwaga warstwa niezależna od sprzętu___
Śmieszna sprawa gdy sprzęt nie pozwala na 3 poziomowe to PMD jest jedno elementowy czyli tak naprawdę nie istnieje.

Liczymy strony.
12 najmniej znaczących bitów to offset 0 - 11
mamy 1024 wpisy
kolejne 10 bitów 
to numer pozycji 2 PTE 12 - 21 -adres początkowej ramki przechowujący daną stronę
od 22 do 31 to pozycja PGD

Mamy 12 wolnych mniej znaczących bitów  zatem są tam bity_ochrony:
PAGE_PRESENT
PAGE_RW
PAGE_USER
PAGE_DIRTY
PAGE_ACCESS 
jest na prezentacji
Rejestr CR3 przechowuję adres tablicy PGD
zmiana wartości CR3 skutkuję wyzerowaniem bufforu TLB ( mapowania straciły sens)
Każda zmiana CR3 jest droga bo zeruję TLB(koszt który musimy zapłacić by przełączyć kontekst)




Nie allokujemy przy procesie tego co nie będzie używane.




Proces jest wyższy niż wątek
wątek nie ma własnej pamięci adresowej
proces ma jeden wątek który słucha i jeżeli przyjdzie jakieś polecenie to tworzy się nowy wątek.
inaczej niż fork który tworzy odrębną przestrzań adresową
mają ten sam indentyfikator przez mm_struct


### DYGRESJA
kod każdej funkcji adresowej zapisujemy w RAM jesteśmy zatem w pamięci fizycznej
Proces 1
koło 3 GB zamapowane są ramki z SO
dla każdego procesu ten sam adres zawiera  zamapowany SO
Wątek jądra może podkraść tablicę procesu bo interesuję go tylko SO nie trzeba przeładować CR3 ani wyzerować TLB


Ileś kawałków procesu jest rezerwowanych na stertę
Proces :
Stos
Kod
.bss
jądro
  :
  .
Sterta 
te elementy zajmują wiele stron


Te struktury są przechowywane w czerwono czarnym drzewie przedziałowym


## Błędy braku strony
Nie ma ustawionego bitu poprawnosci odwołania - brzmi jak bullshit
Poboczne błedu braku strony:
- odwołanie do niepoprawnego obszaru rozszerzalnego
- rozszerzanie dziej się dynamicznie
- zapis do strony- sprawdź czy nie jest to COW ( copy on write)
Wszystkie strony obszaru .bss są mapowane jako 0 i jest zamarakowane jako 0.
Jeżeli chce zmienić wartość to jest tworzona nowa ramka z której jest zapisywany nowa wartość i już odczyt jest niezerwoy.



## strategia wymiany stron
linux 
dwie strukturki FIFO i Kolejka Cykliczna
Strony potrzebne są zarządzane cykliczną jeżeli jest nipotrzebne te są zrzucane do FIFO












Na egzamienie:
- schemat pamięci wirtualnej
- rodzaje stron
- optymalizacji
- strategie wymiany stron
- anomalia beladiego
- dokładny schemat co robi przy odwołaniu do pamięci wirtualnej
- Jak to sie dzieje ze DMA co robi
- migotanie stron problemy z tym zwiazane 

## Uwaga gadamy o architekturze 32 bitowej, żeby było łatwiej



