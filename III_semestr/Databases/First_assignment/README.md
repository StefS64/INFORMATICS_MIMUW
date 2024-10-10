W załączniku jest plik CSV zawierający zbiór danych o publikacjach pracowników Instytutu Informatyki z lat 2017-2020. Zadanie polega na: stworzeniu tabeli z odpowiednimi kolumnami; wstawieniu do niej tych danych; wypisaniu (za pomocą zapytania) wszystkich prac opublikowanych w roku 2020, w których średnia liczba punktów na autora wynosi 100 lub więcej, posortowanych po liczbie autorów; a na koniec skasowaniu stworzonej tabeli.

Oddać należy pojedynczy plik tekstowy zawierający: 

-   jedną instrukcję CREATE TABLE (tworzenie tabeli), 
-   ciąg instrukcji INSERT (wstawienie danych),
-   jedną instrukcję SELECT (zapytanie),
-   jedną instrukcję DROP TABLE (usuwanie tabeli).

Plik musi się poprawnie wykonywać przy wczytaniu poleceniem 

*SQL> @plik_do_zaladowania.sql *

w bazie Oracle (analogicznie dla postgresa).
