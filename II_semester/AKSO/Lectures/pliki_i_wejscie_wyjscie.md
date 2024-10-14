# Urządzenia wejścia wyjścia
Podział na dwie kategorie w unixie 
- Urządzenia blokowe
informacje przechowywane na blokach 
urzadzenie zapisuje i odczytuje każdy blok niezależnie.
- Urządzenia znakowe
operuję się na poziomie bajtów ( np myszka)
Niektóre nie pasuja do tych podziałach


## Rodzaj dostępu
Dostęp Sekwencyjny 
- czyta się znak po znaku i nie może się przewijać
Dostęp Swobodny 
- może zapisać i odczytać w dowolnym miejscu


## Rodzaj Komunikacj
- synchroniczny 
Sciśle określony moment pojawienia się w czasie.
- asynchroniczny
Porcje danych pojawiają się w dowolnym momencie czasu np myszka


## Sposób współdzielenia
- wyłaczny dostęp
tylko jeden proces na raz
- współdzielenie
wiele procesów może korzystać z wariata




## Moduł wejścia wyjścia 
ma budowę warstwową
Warstwa SO niezależna od urządzenia 

## Geomtria Dysku ( było)





## KOmunikacja komputera z sterownikiem
Jest interfejs
Sterownik posiada trejestry
Komunikacja aktywna lub w sposób przerwaniowy


Odbiór lub wysłanie do jakiegoś urządzenia często wiarzę sie z odczytaniem jakiegos adresu.
Rzeczy zależny od konkretnych urządzeń są ukrytę w jakiejś warstwie abstrakcji





# systemy plików 
Plik - logiczny pojemnik na dane

Każdy sytem plków udostępnia ten sam interfejs

Jądro nie interpretuje zawartości pliku
Jądro udsotępnia operacje do organicaxji nazywnaia i kontroli()  plików
plik indentyfikuję się z pomocą jego nazwy ścieżkowej
ścieżka względna jest wyliczana przez katalog roboczy procesu(ten katalog gdzie odpalany jest proces) zaczynamy bez ukośnika
jeżeli zaczynamy od /etc to lecimy od "korzenia"




Dowiązanie odniesienie do pliku w katalogu

Nazwa pliku nie identyfikuje tego pliku

mamy przykładowo 2 podkatalgi A i B wykorzystuję się ln jest to dowiązanie 
w A moje.txt
jak odpalimy w B to następnie te pliki będą edytowane równolegle
rm - nie usuwa całego pliku tylko dowiązanie
root może tworzyć dowiazania sztywne nawet katalgów urzytkownik jedynie plików

ln -s dowiązanie symboliczne ( pointer do pliku jest wtedy w tym plik)
Usuniecie pliku doprowadzi że dowiazanie symboliczne będzie wkazywać na nic
Same usuniecie skrótu oczywiście nic nie przeszkadza.



## Atrybuty pliku

Węzeł indeksujący inode  i-węzeł

Numer i-węzła jednozacznie definuje plik
Powszechne atrybuty pliku:
typ
liczba dowiązań
rozmiar pliku
identyfikator urządzenia na którym plik jest
numer i- węzła
właściciel pliku
prawa dostępu do pliku
znaczniki czasowe




___
W i-węźle jest przechowywan informacja gdzie szukać tych danych na dysku__
10 pierwszych pozycji trzyma położenie pierwszych 10 bloków tego pliku.
W przypadku dłuższych pliku blok 10 trzyma pozycję bloków dalszych bloków. 
tak jak w stroniccowaniu 256
pozycja numer 11 trzyma kolejną klasę abstrakcji 256 * 256_każdy blok ma 1 kilo
pozycja numer 12 trzyma jeszcze kolejną klasę abstrakcji
