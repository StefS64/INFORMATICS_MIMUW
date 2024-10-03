# Testerka do zadania dzie
## Co co robi?
- `gen.py` zawiera kod do generowania pojedyczego testu
- `generator.py` generuje test do pliku (składnia opisana w mdiv_example.c), skaładnia:
```
generator.py N TYPE file SEED ID"
```
- `Makefile` jak sama nazwa mówi, makefile
- `test.sh` skrypt do testowania, sam kompiluje i takie tam. Może trzeba mu nadać uprawnienia.
- `mdiv_example.c` lekko zmodyfikowany mdiv_example. Wcztuje z stdin dane w formacie opisanym w środku. jeśli test kończy się błędem to wypisze gdzie co i jak, inaczej milczy.
- `tester` zlinkowany mdiv_example.c z mdiv.asm

## Instrukcja obsługi
1. Skopiuj plik `mdiv.asm` do tego folderu.
2. Odpal `test.sh`
3. Jeśli test się wywali, testerka powinna zakończyć swój żywot i w `test_input.txt` powinno być wejście. Można wkleić do `mdiv_example.c` z zadania i debugować.
3. Koniec.

## Uwagi
### Generowanie
Opisane jest dokładniej w `generator.py`. 
Wybieramy stały zakres, z którego losuje liczby (przy interpretacji NKB dla 64 bitów).
Testerka potem ustala na podstawie MSB znak i liczy wynik.
Dane do `mdiv_example.c` są przekazywane jako inty (ze znakiem już).

### Paczki
Są trzy:
1. **SMOL** - małe testy.
2. **FULL** - pełny zakres, full wypas.
3. **Overflow** - overflow.

### Overflow
Przygotowałem specjalną paczkę na overflowy.
Generuje ona przypadki z overflow'em i sprawdza dla kolejnych długości x (czyi n'ów).
Jeśli overflow wystąpi to powinen pojawic się dodatkowy komunikat przy każdym teście, w stylu:
> Błąd obliczeniach zmiennoprzecinkowych.



## Kończąc
Przepraszam za błędy, braki pewnych liter w komentarzach (aj...).
Testowałem na swoim rozwiązaniu testerkę i starałem się, aby dało się ją łatwo dostosować do swoich potrzeb.
W `test.sh` oraz `generator.py` można zobaczyć jak dokladniej używać testerki.

> Będę wdzięczny za zgłoszone błędy, których niestety może być trochę.
