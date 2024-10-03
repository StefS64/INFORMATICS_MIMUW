Bramki NAND
-----------

Zadanie polega na zaimplementowaniu w języku C dynamicznie ładowanej biblioteki obsługującej kombinacyjne układy boolowskie złożone z bramek NAND. Bramka NAND ma całkowitą nieujemną liczbę wejść i jedno wyjście. Bramka NAND bez wejść daje na wyjściu zawsze sygnał o wartości `false`. Bramka NAND o jednym wejściu jest negacją. Dla dodatniego `n` wejścia bramki `n`-wejściowej są numerowane od `0` do `n - 1`. Na wejście bramki można podać sygnał boolowski przyjmujący wartości `false` lub `true`. Na wyjściu bramka daje sygnał `false`, jeśli na wszystkich jej wejściach są sygnały `true`, a `true` w przeciwnym przypadku. Sygnał z wyjścia bramki można podłączyć do wielu wejść bramek. Do wejścia bramki można podłączyć tylko jedno źródło sygnału.

Interfejs biblioteki
--------------------

Interfejs biblioteki znajduje się w załączonym do treści zadania pliku `nand.h`, który zawiera poniższe deklaracje. Dodatkowe szczegóły działania biblioteki należy wywnioskować z załączonego do treści zadania pliku `nand_example.c`, który jest integralną częścią specyfikacji.

```
typedef struct nand nand_t;

```

Jest to nazwa typu strukturalnego reprezentującego bramkę NAND. Typ ten trzeba zdefiniować (zaimplementować) w ramach tego zadania.

```
nand_t * nand_new(unsigned n);

```

Funkcja `nand_new` tworzy nową bramkę NAND o `n` wejściach. Wynik funkcji:

-   wskaźnik na strukturę reprezentującą bramkę NAND;
-   `NULL` -- jeśli wystąpił błąd alokowania pamięci; funkcja ustawia wtedy `errno` na `ENOMEM`.

```
void nand_delete(nand_t *g);

```

Funkcja `nand_delete` odłącza sygnały wejściowe i wyjściowe wskazanej bramki, a następnie usuwa wskazaną bramkę i zwalnia całą używaną przez nią pamięć. Nic nie robi, jeśli zostanie wywołana ze wskaźnikiem `NULL`. Po wykonaniu tej funkcji przekazany jej wskaźnik staje się nieważny.

Parametr funkcji:

-   `g` -- wskaźnik na strukturę reprezentującą bramkę NAND.

```
int nand_connect_nand(nand_t *g_out, nand_t *g_in, unsigned k);

```

Funkcja `nand_connect_nand` podłącza wyjście bramki `g_out` do wejścia `k` bramki `g_in`, ewentualnie odłączając od tego wejścia sygnał, który był do niego dotychczas podłączony.

Parametry funkcji:

-   `g_out` -- wskaźnik na strukturę reprezentującą bramkę NAND;
-   `g_in` -- wskaźnik na strukturę reprezentującą bramkę NAND;
-   `k` -- numer wejścia bramki `g_in`.

Wynik funkcji:

-   `0` -- jeśli operacja zakończyła się sukcesem;
-   `-1` -- jeśli któryś wskaźnik ma wartość `NULL`, parametr `k` ma niepoprawną wartość lub wystąpił błąd alokowania pamięci; funkcja ustawia wtedy `errno` odpowiednio na `EINVAL` lub `ENOMEM`.

```
int nand_connect_signal(bool const *s, nand_t *g, unsigned k);

```

Funkcja `nand_connect_signal` podłącza sygnał boolowski `s` do wejścia `k` bramki `g`, ewentualnie odłączając od tego wejścia sygnał, który był do niego dotychczas podłączony.

Parametry funkcji:

-   `s` -- wskaźnik na zmienną typu `bool`;
-   `g` -- wskaźnik na strukturę reprezentującą bramkę NAND;
-   `k` -- numer wejścia bramki `g`.

Wynik funkcji:

-   `0` -- jeśli operacja zakończyła się sukcesem;
-   `-1` -- jeśli któryś wskaźnik ma wartość `NULL`, parametr `k` ma niepoprawną wartość lub wystąpił błąd alokowania pamięci; funkcja ustawia wtedy `errno` odpowiednio na `EINVAL` lub `ENOMEM`.

```
ssize_t nand_evaluate(nand_t **g, bool *s, size_t m);

```

Funkcja `nand_evaluate` wyznacza wartości sygnałów na wyjściach podanych bramek i oblicza długość ścieżki krytycznej.

Długość ścieżki krytycznej dla sygnału boolowskiego i dla bramki bez wejść jest równa zeru. Długość ścieżki krytycznej na wyjściu bramki wynosi 1+max(S0,S1,S2,...,Sn-1)

, gdzie Si jest długością ścieżki krytycznej na jej i

-tym wejściu. Długość ścieżki krytycznej układu bramek jest to maksimum z długości ścieżek krytycznych na wyjściach wszystkich podanych bramek.

Parametry funkcji:

-   `g` -- wskaźnik na tablicę wskaźników do struktur reprezentujących bramki NAND;
-   `s` -- wskaźnik na tablicę, w której mają być umieszczone wyznaczone wartości sygnałów;
-   `m` -- rozmiar tablic wskazywanych przez `g` i `s`.

Wynik funkcji:

-   długość ścieżki krytycznej, jeśli operacja zakończyła się sukcesem; tablica `s` zawiera wtedy wyznaczone wartości sygnałów na wyjściach bramek; `s[i]` zawiera wartość sygnału na wyjściu bramki wskazywanej przez `g[i]`;
-   `-1` -- jeśli któryś wskaźnik ma wartość `NULL`, `m` jest równe zeru, operacja nie powiodła się lub nie udało się alokować pamięci; funkcja ustawia wtedy `errno` odpowiednio na `EINVAL`, `ECANCELED` lub `ENOMEM`, a zawartość tablicy `s` jest nieokreślona.

```
ssize_t nand_fan_out(nand_t const *g);

```

Funkcja `nand_fan_out` wyznacza liczbę wejść bramek podłączonych do wyjścia danej bramki.

Parametr funkcji:

-   `g` -- wskaźnik na strukturę reprezentującą bramkę NAND.

Wynik funkcji:

-   liczba wejść bramek podłączonych do wyjścia danej bramki, jeśli operacja zakończyła się sukcesem;
-   `-1` -- jeśli wskaźnik ma wartość `NULL`; funkcja ustawia wtedy `errno` na `EINVAL`.

```
void* nand_input(nand_t const *g, unsigned k);

```

Funkcja `nand_input` zwraca wskaźnik do sygnału boolowskiego lub bramki podłączonej do wejścia `k` bramki wskazywanej przez `g` albo `NULL`, jeśli nic nie jest podłączone do tego wejścia.

Parametry funkcji:

-   `g` -- wskaźnik na strukturę reprezentującą bramkę NAND;
-   `k` -- numer wejścia.

Wynik funkcji:

-   wskaźnik typu `bool*` lub `nand_t*`;
-   `NULL`-- jeśli nic nie jest podłączone do podanego wejścia; funkcja ustawia wtedy `errno` na `0`;
-   `NULL`-- jeśli wskaźnik `g` ma wartość `NULL` lub wartość `k` jest niepoprawna; funkcja ustawia wtedy `errno` na `EINVAL`.

```
nand_t* nand_output(nand_t const *g, ssize_t k);

```

Funkcja `nand_output` pozwala iterować po bramkach podłączonych do wyjścia podanej bramki. Wynik tej funkcji jest nieokreślony, jeśli jej parametry są niepoprawne. Jeśli wyjście bramki `g` jest podłączone do wielu wejść tej samej bramki, to ta bramka pojawia się wielokrotnie w wyniku iterowania.

Parametry funkcji:

-   `g` -- wskaźnik na strukturę reprezentującą bramkę NAND;
-   `k` -- indeks z zakresu od zera do wartości o jeden mniejszej niż zwrócona przez funkcję `nand_fan_out`.

Wynik funkcji:

-   wskaźnik typu `nand_t*` na bramkę podłączoną do wyjścia bramki `g`.

Wymagania funkcjonalne
----------------------

Wyznaczenie wartości sygnału i długości ścieżki krytycznej na wyjściu bramki wymaga rekurencyjnego wyznaczenia tych wartości na jej wejściach (choć nie wymagamy rekurencyjnej implementacji), chyba że bramka nie ma wejść. Wyznaczenie tych wartości może się nie udać, jeśli do któregoś wejścia nie jest podłączony żaden sygnał, procedura się zapętla (bramki nie tworzą układu kombinacyjnego) lub wystąpi błąd alokowania pamięci.

Należy zadbać, aby wyznaczenie wartości sygnału i długości ścieżki krytycznej na wyjściu bramki było wykonane tylko raz.
