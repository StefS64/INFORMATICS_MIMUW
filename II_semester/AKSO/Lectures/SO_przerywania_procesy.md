# Systemy operacyjne
- Uwaga notatki przy głowie pękającej(ale nie piłem wczoraj) 

## Jest...

- program, który zarządza systemem komputerowym, baza programów urzytkowych oraz pośrednik między użytkownikiem a sprzętem komputerowym
~ Abraham Silverszatz


- Jądro systemu operacyjnego trzyma podstawowe własności typu odczyt danych, zakończenie programu.
- Main to commenda odwołująca się do exita systemu operacyjnego

### zarządcą(zasobami systemu)
- zajmuje się sytuacjami patowymi typu scanner/drukarka


### zadania SO
#### ukrywa szczegóły systemu operacyjnego 
- równoległości - możliwa praca współbueżna 
- pamięci- pamięć wirtualna
- urządzeń - jednolity sposób dostępu

#### zarządza zasobami 
- decyduje, które procesy mają się wykonać
- przydziela pamięć wykonującym się procesem
- zapobeiga zakleszczeniom


#### zapewnia bezpieczną i wygodną pracę poprzez:
- interfejs użytkownika
- mechanizmy ochrony dostępu 



### Dobry sytem operacyjny, którego użytkownik nie jest świadom jego istnienia 


## Połączenia sprzętu i system uoperacyjnego 
- driver powinien istnieć do nowego sprzętu( niestety linux czasem nie wspiera jakiegoś sprzętu )






## Funkcje systemowe:

- programy są napisane na funkcjach systemowych
- funkcje te są ogólne i tworzą środowisko łatwiejsze do wykorzystania
- numer exita jest przekazywany do funkcji systemowej by wiedziec jakie przerwanie zrobić
## Składowe systemyu komputerowego:

Użytkownicy:        1        2 

  |        |        |        |
Języki, programy(assembler, .txt, 

    \   |    /   
Programy systemowe 

        |
System operacyjny
        | 

Sprzęt komputerowy


## Przykłady:

### składowe systemu operacyjnego:

- interpreter 
- jakies inne syfki


## definicje:


### Jądro 

- pakiet niezbędnych do działania funkcji
 
### Jednorodność:
- systemy operacyjny przesłanoia zawiłości sprzętowe, pozwala aplikacjom postrzegać w jednolity sposó sprzęt
- tymi samymi narzędziami operujemy na tym komputerze.

### wirtualizacja
- System operacyjny może stwarzać wrażenie istnienia w komputerze pewnych zasobów lub nie zapisywać niektórych rzeczy od razu.
- przykładowo komputer nie zapisuje od razu wszystkiego tylko oszukuje mając million znaczków do wczytania on buforuje te znaczki 
- dobrze widać ten problem jak się usuwa pendriva bez bezpiecznego wyjęcia to dane mogą jeszcze nie być zapisane.


- Stwarzanie pozoru istnienia wielu zasobów przykładowo mając jeden procesor, który może wykonywać swiele procesów jednoacześnie. 
- System tworzy coś takiego jak pamięć wirtualna( korzysta z pamięci głównej i dysku)

### wieloprogramowość
- pojawia się współbieżność 
- jakiś dedykowany do zadania element a procesor zajmuje się czymś innym.
- nowy program tylko wykonuje się gdy drugi przestaj działać.

### wielozadaniowość 
- od razu wpuszczamy parę procesów
- każdemu dajemy kwant czasu(po upływie tego kwanta procesor zajmuje się następnym zadaniem)
- Tak jak studiowanie xd(PO - AKSO - ANALIZA -ANALIZA -ANALIZA - GAL - GAL - GAL -ANALIZA -ANALIZA -b -JAO - ANALIZA)
- po zakończeniu wszytsku=ich kwantów one wracają od początku

## Irytujące rzeczy:

- Nie ma powszechnej zgody na temat tego, co jest częścią a co nie systemu operacyjnego.
# Systemy:

## Systemy dla komputerów osobistych :

- nacisk na wygodę 
-stopniowe wprowadzanie technik w dużych maszynach.

##systemy rozproszone

- procesy luźno związane 
komunikują się przez złącza komunikacyjne

##systemy wbudowane

- wyspecializowane systemy nadzorujące różne urządzenia 
- interakcja osprzętu i sprzętu( bucket list )

## Systemy czasu rzeczywistego 

- systemy z zwięzłym czasem( bucket list zepsuć piekarnik)


# Podstawoe pojęcia 
## Proces
- wykonanie programu
- byt aktywny który ten program wykonuje
- niektóre procesy mogą wykonywać ten sam program.
- dostaje pid jakiś adres 
- zbiór rejestrów
- na dzień dobry proces ma 3 pliki wejście wyjście i wyczytywanie procesów
- każdy plik ma dskryptor
- stan procesu

### Pamięć procesu
ma:
- kod programu
- dane 
- stos procesu
Jest adresowana od zera (jest to tak naprawdę tablica/ nie cała jest adresowana jako pamięć oczywiście ).
Odwołanie do adresu zawsze jest adresem logicznym( dopiero potem są odwzorywane na prawdziwą pamięć).
***Jest Sprzętowo aodwzorowana na fizyczną pamięć***
Procesore achowuje sobie miejsce gdzie pamięta gdzie skończył w pamięci.

### Procesor podstawowe fakty:
- ma rejestry bardzo mała szybka pamięć
Nie musi znajdować się w pamięci fizycznej.
### Tablica procesów
- ma blok kontrolny/ metryczka procesu - duża struktura która ma zarezerwowaną pamięć.
- W tej tablicy zachowywane są informacje o procesach
- Dla roota zawsze są zarezerwowane elementy.( musi być rezerwa aby root mógł zawsze jakieś procesy zrobić (żeby np w przypadku zapchania))

Metryczka obrazek:
-----
	Stan procesu
	Numer procesu
	Licznik rozkazów 
	Rejestry
	Rejestry zarządzania pamięci
	Deksryptory Otwatych plików
	 ...(gdzieś tu ma 2 wskaźniki w przód i w tył by można było go podłączyć)
------

### Stany procesu
- jak jedna nóżka obsługuje dużo przerwań
- jest kolejny układ sprzętowy zwany Kontrolerem przerwań
Dawniej był jeden kontroler i po prostu wrzucał numerek w "pamięć"
- aktualnei przy architekturze wielordzeniowej jest ciekawiej

Diagramik:         _______         ______> zakończony (zakończone ale jeszcze trwają bo ich potrzebujemy stają się zombii)               
                  /       \>      /        
Nowy ---- > gotowy         Aktywny ----- > wstrzymany( czeka na pamięć) kiedy on teraz wróci do prcoesu gotowego(czeka na DMA) 
             <\   <\______/                 /         
               \___________________________/   

Gotowy - ma już wszytstko oprócz procesora
Aktywny - ma kwant czasu procesora jak się skończy staje się gotowy
Wstrzymany - czeka na pamięć( został przerwany przez DMA) Oczywiście nie musi to być jedyny powód.
Zakończony - już nie działa ale jeszcze istnieje to czeka na odczytania kodu zakończenia.

Mamy kilka kolejek w wstrzymanych jaki gotowych.


Każdy osierocony proces jest adoptowany do procesu o numerze 1 gdy rodzic zakończy proces bez odczytywania kodu zakończenia

### Podział procesów ze względu na wykorzystanie procesora 

- Proces ograniczony przez wejście i wyjście 
- Proces ograniczony przez procesor( potrzebuje długich faz procesora







### Uwagi do procesów
Jak proces A i B odwołają się do miejsca 100 to najpewniej trafią w inne miejsce ( zwykle procesy są od siebie niezależne ( mają rozłączną pamięć adresową))





### Przełączenie kontekstu
- zmiana procesu i wykonanie następnego
- proces może nie wykorzystać całego kwantu czasu

#### Realizacja 
- task: state segment, state segment descriptor, gate descriptor, register.
- przekazanie sterowania do innego procesu powoduje automatyczne zapisanie.
- ogranicznie na liczbę procesów jednocześnie.
- Sterowanie przerwaniami:

- Za przełącznie odpowiedzalny jest jądro jakaś wpudowana funkcja SO

## Przerwania
Generowana przez operacje wejścia wyjśccia( DMA - przy wejściu/wyjściu.
Pojawia się asychronicznie, czyli w dowolnym momencie.

W procesorze odpowiedzialne za to są niektóre nózki( pojawia się wysokie napięcie przy ważnej sprawie)
- tworzone sprzętowo
- obsługiwane softwarowo

### wykrycie przerwania
- adres w którym było wykonanie zostaje zapamiętany
- proces nie powinien zauważyć wykonania przerwania


#### przerwanie przerwania
Uwaga zależy od architektury.
1. priorytety
2. w czasie wykonywania przerwnia nie ma przerwań
3. można przerwać przerwanie  
### Źródła przerwań
- istnieje coś takiego jak tablica przerwań
- każdy elemen jest numerowany i w taki sposób jest rozpoznawany numer przerwania i już wiadomo gdzie ją wrzucić
- przy butowaniu systemu jest generowana tablica przerwań
- są też przerwania między procesowe.
### funkcja przypisania
- musi oczywiście nie ingerować w taki sposób by zmieniać rzeczy, że został przerwany
- często proces nie wie czy został przerwany

## Wejście-wyjście 

- SO jedynie inicjuje operacje czyli komunikuje się z DMA - Direct Memory Access, procesor zajmuje się innymi rzeczami
- DMA osobny układ sprzęt zajmujący się pamięcią po zakończeniu zadania daje do procesora przerwanie.

# Ochrona

2 tryby pracy:
- tryb użytkownika
- tryb uprzyliwejowany
tłumaczą się same.

## SO może wszystko
- przerwanie powoduje zmiane trybu wykonania

## Poziomy ochrony:
0 - Jądro
1 - System
2 - roszerzenia 
3 - aplikacja
w normalnych sytemach są tylko 2 
jądro i aplikacja

UWAGA - nie ma rozkazu który pozwala się przełaczyć z uprzylianego do nie i na odwrót.

## **Przykład** realizacji ochrony pamięci:
wprowadznenie dwóch rejestrów:
- bazowy
- graniczny
zpamiętuje początek i koniec pamięci
musi być pomiędzy dolną a górną granicą to jest sprawdzane ***sprzętowo!!!*** nie softwarowo jest to szybkie i sprawdzane na poziomie procesora.



## Mechanizm wywołania systemowego 
Proces użytkownika 
proces użytkownika
wywołanie systemowe( przerwanie softwarowe)
Przy powrocie z przerwania wracamy do trybu użytkownika.



 
Przykład:
ktoś napisał program bash i na tym programie wykonuje się proces
## Progam
- coś statycznego zbiór instrukcji
##przestrzeń adresowa







# Szeregowanie


## kiedyś:
- szeregowanie krótko terminowe 
- długo terminowe teraz już nieistotne nie występuje w dzisiejszych czasach
## Szeregowanie krótkoterminowe 

###Decyzja o przydziale procesora:
- aktywny proces na zmiae na gotowy
- aktynwny proces na wstrzymany
- wstrzymany/nowy zmienia się na gotowy( proces ważniejszy może przyjść i trzeba znów rywalizować)
- aktywny proces kończy się 

### Szeregowanie wywłaszcające
- ktoś siłą zbierą zasób, który był przydzielony procesowi


- UWAGA trzeba chronić przed przerwaniem ( przykład listy która się rozspujnia przez jeden z procesów a potem drugi na nim operuje)

- KOniecznoność synchronizowania dostępu do danych dzielonych.

#### Co jeżeli proces wywłaszczany wykonuję operację jądra (przykładowo kończy mu się kwant czasu)

- Jądro wywłaszczalne

AKtualnie to jest wykorzystywane jest możliwość wywłaszczenia jądra
Zmniejsza czas reakcji procesów

- Jądro niewywłasczalne

Rozumie się samo przez się czekało do dokończenia tego procesu chyba, że jest to operacja wejścia wyjścia. ( preoces jądra może decydować kiedy, chce się przerwać)



### Szeregowanie niewywłaszczające

- proces może z niego korzystać dopóki zdecyduje inaczej 
Wstrzymanie ok bo sam procesor czeka na wejście
wstrzymany na gotowy(to jest wywłaszczenie
Zostaje jedynie:
- proces zmienia się na wstrzymany
- proces kończy się 



#### Dlaczego?
- W starszych kompa to ma więcej czasu bo nie był timerów i nie byliśmy wstanie odmierzyć kwantu czasu.
- Łatwiej operować na elementach, które są współdzielone.
- Ale minus komputer może liczyć dzień i nie można nic z tym zrobić.

## Cele szeregownia:

### Ogólnie:
- zagwarantowanie uczciwego dostępu do prcesora
- dbanie o równowagę procesu + równomierne obciążenie procesu
- realizacja założonej polityki

### w syst WSADOWYCH

### W syst interaktywnych 
- zagwarantowanie szybkiego czasu odpowiedzi
- minimaliazacja czasu wariancji odpowiedzi ( czyli, żeby nie było dużych wachań w czasie realizacji procesu/ lepsze jest np w telewizorze jest lepiej poczekać co jakiś czas na następną ramkę niż, żeby się zacinało)

### Linux
- programiści się się spiewali, który algorytm szeregujący wykorzystać
___NA egzaminie będą nazwy pełne algorytów
____
##algorytmy:


###FCFS ( first come first served)
- jest to strategia nie wywłaszcająca.

- Robimy kolejkę 
- procesy jedynie czekają przy wejściu wyjściu.
- wykorzystywany na linuxie ale tylko przez roota 



#### Wnioski:
- łatwy do zromienia
- średni czas oczekiwania może być długi
- DUża wariancja czasu oczekiwania
- efekt konwoju:
	- Proces $P1$ ograniczony przez procesor
- Nie nadaj się do systemów z podziałem czasu




### SJB ( shortest job first )

- szeregowanie niewywłaszczające 
- Zadanie o najakrótszej kolejn ej fazie
- ALgorytm zachłannny
- Optimalizacja średniego czasu oczekiwania

####przykład
20
2
2
4
czas oczekiwania:
8
0 
2 
4
średni to :

#### pratkyka:
ciężko przewidzieć czas działania fazy.

### SRTF( shortesttune remianing time first )
- wmiarę oczywisty:
Przykład jest na  slajdach.
Uogólnienie SJF

#### priorytety mogą być:
- wewnętrzne 
- zewanętrzne - pryzdzielane przez SO

#### Możliwość zagłodzenia
- proces o jakimś długim działaniem może nie zostać wybrany 
- wykorzystuje się ageing czyli wracają do 


### Round-robin ___ Dopisać z wykładu.
- wywłaszczająca wersja FCFSa
- szeregowanie rotacyjne
kwant czasu stały i zawsze wykorzystuje tyle samo.

#### implementacja: 
- konieczne jest wsparcie sprzętowe ( czasomierz generujący przerwania)

#### wydaajność
- maznujemy czas na przełączenie kontekstu 
Jaki powinien być kwant czasu?
- gdy kwant jest bardzo długi to: FCFS = RR.
- przełaćzeni kontekstu wymaga czasu
-kwant czasu powinien być długi w proównaniu z czasem przełączenia kontekstu.



- gdy jest bardzo krótki to RR = TS( teoretycznie mały kwant działają "wspólnie").

### LOTERYJKA
- losuje który program ma dostać kwant czasu

### przykładowo można zrobić tak, że użytkownicy mogą decydowac, które procesy mają więcej "losów"


### inna strategia procesy wstrzymane które wykorszystał y mały kwant czasu ą wrzuacane bliżej początka

## Szeregowanie wielopoziomowe
###OSobne kolejki dla poszczególnych grup procesów np:
-procesy czasu rzeczywistego
-procesy systemowe



### sprzężenie zwrotne
- przesuwanie procesów względu priorytetu
- spada oczko niżej gdy wykorzysta szybko kwant czasu
- procesy interaktywne sa wykorzystywane wcześniej niż obliczeniówki one 
strzałki w dół i w góre pomiedzny wieloma poziomam kolejki. 
Pozwalają przykładowo:

 w lniuzie jest ich z 200
np:

3 kolejki:
 


10 kwant

20 kwant 

FCFS 


## Ekspedytor

Musi być szybki

Odpowiedzialny za przekazywanie procesorowi odpowiednich rzeczy:

- przełączenie kontekstu
- wejście w tryb użytkownika
- wykonanie akou w odpowiednie miejsce kodu programu









# Miary czasu: ___Uzupełnić z slajdów

## wykorzystanie procesora

## Przepustowość

## Czas obrotu
- czas od wejścia do wyjścia procesu
## Czas Oczekiwani
- czas jaki procesor spędizł w kolejce

## Czas odpowiedzi



# Klasy algorytmów szeregujących:


___Przypomnienie - systemy wsadowe ( te stare) procesy mogły utrzymywać proces na dłużej.

____
## Systemy interakcyjne 
- NIezbędne wywłaszcenie





