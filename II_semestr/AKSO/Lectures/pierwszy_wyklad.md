Architektura komputerów i systemy operacyjne
28.02.2023
Zasady zaliczenia:
Nie ma ćwiczeń xd
3 zadania zlaiczeniowe
-pierwszy większy
20pkt
-2 kolejne assemblerowe projekty 
2x10 punktów za zadania
+egzamin(charakter teoretyczny bez notatek
-będzie a propo zrozumienia
-test wielokrotnego odpowiedzi
-okropna forma jak na WMach 3 podpunkty P/F
-2x5pkt pytania otwarta


60% gwarantuje pozytywną ocenę poniżej 50% negatywna 


# Co będzie:

opowieść dwu częściowa:

-składanie komputera - dowiemy się jak jest zbudowane i będzie można się pobawić
-w przyszłym tygodniu tworzymy sobie procesor


-Zobaczymy sobie kod maszynowy zobaczymy jak procesor jest zbudowany. 
-systemy operacyjne + assembler- nauczymy się jak na 64 bitowej structurze intellowskiej.


Na labach: 
bash
wywoływanie funkcji procesowych, jak działa printf i scanf.
assembler



# Wykład nr 1. - składamy komputer

## Procesor
-pod procesorem są złącza, którymi łaczy się z światem, zewnętrznym, jest ich sporo xd.
-parametry:
	-liczba rdzeni(ilość niezależnych procesorów w nowszych procesorach nie są symetryczne, niektóre są zażądzające)
	-częstotliwość taktowania(napięcie zmienia się cyklicznie, względem zagera)UWAGA procesor się grzeję im częstsza częstotliwość taktowania) taktowane rzędyu 3-4 ghz
Jest wyposażony w pamięć podręczną( odczytuję co chce zmienić wrzuca do pamięci podręczenej i nie musi tego często odczytywać)


## Płyta Główna
Zawiera:
-gniazdo na procesor(złącza muszą być połączone)/gniazda się różnią względem producenta(muszą być kompatybilne)
-wielowarstwowa płytka drukowana łączy procesor z innymi elementami
-gniazda rozszerzeń(miejsce na karte graficzną/dedykowna karta dźwiękowa/rozszerzenia wejścia, wyjścia)
-gniazda eternetu, USB(kotroler USB bezpośrednio na płycie), układ odpowiedzialny za eternet(kontroler)
-gniazda pamięci
Trzeba:
podłaczyć do przycisków w obudowie startu by odpalić


## Pamięć RAM:
-kawałek blachy dokola, służące do chłodzenia.
-obecne płyty główne oferują dual channel memory(łącząc do tego samego koloru dostęp do pamięci jest szybszy(jest wstanie dostać się do niej 2 razy w tym samym czasie ))
-UWAGA do poprwaności(składania) - powinno się być uziemiony i wtedy dopiero wziąć za koputer. 
-są kondensatorki które są ładowana i muszą być ładowane co jakiś czas
-jest sterownik pamięci który służy do odświeżania pamięci 

## Chłodzenie procesora
-Pasta termoprzewodząca
-wiatrczki - są montowane na radiatorach mają pomoć odprowadzić ciepło
-radiator pozwala odprowadzić ciepło
(zamontoewanie stellarzu we własnym zakresie)

## Karta graficzna
-mały komputerek niezależna od procesora
-można podłączyć ¶ównolegle 2 tych smaych producentów.
radiatory to te blaszki a pipy to rurki odprawadzające
-własny system chłodzenia(radiator) 

## Zasilanie
Ważny jest zasilacz o odpowiedniej mocy by zasilicz kartę graficzną i płytę główną oraz dyski twarde.
-podstwoway parametr to moc 


## Dysk Twardy 
-pamięć masowa służaca do przechowywania danych nieulotna
Budowa:
-są srebne talerze które cały czas się obracają 7200 obrotów na minutę
-głowica odczytująca i zapisująca(wchodzi między dyski odczytuję informację magnetycznymi)
-jest to urządzenie mechaniczne które odczytuję przsuwając się do miejsca pamięci
  ___
 /  /\ - głowica i dysk 
|  .  |
 \___/__
Wewnętrzna magistrala(taktowana rzędu 100Mhz)
łaczy procesor, pamięć 


## Mostki
-pośredniczą pomiędzy procesorem a urządzeniami zewnętrznymi
(mostek północny - już nie używany wszystko jest już w procesorze łaczył się z urządzeniami szybki np ( pamięcią))
mostek południowy - komunikacja z urządzenieami wejścia wyjścia z procesorem. 



## System operacyjny
Zadaniem systemu operacyjnego ma za zadnie nie odczytywanie dysku rzadku lub żeby robić to szybko.
System nie robi rzeczy dopóki nie musi by ograniczyć zapisywanie na dysku.



# Historia komputerów:
pierwszym komputerem było uważane, że był eniak.
-duża maszyna zajmująca wiele pomieszczeń.
-problemem był średni czas bezawaryjnego pracowania ~30 min
-klasyka trzeba było sobie zarezewować czas użycia komputera.
-programy były zapisywan na taśmach.
Potem:
-zatrudnienia operatora do obsługi komputera\
-nośnik papierowt, służaca do perforacji kodownaie poptrzez dziurkowanie. 
-były maszyny dod pisania do programowania xd
-istniał czytnik do kart perforowanych
-nie było miesjcana methodę prób i błędów.
Jednoprocesowa maszyny obliczeniowe


Taśmy perforowane, większ możliwość  programowania.
-wyjście albo drukarka lub spowrotem taśma perfowrowana. 
1000 razy lepsze są kompy aktualnie.
Adres był złożony z 16 bitów 
MSdos - firmy Microsoft system operacyjny.
