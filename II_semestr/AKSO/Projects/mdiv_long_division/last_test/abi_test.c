#include <assert.h>
#include <inttypes.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZE(x) (sizeof x / sizeof x[0])

const int64_t zlota_liczb = 0x6969696969696969;

// To jest deklaracja testowanej funkcji.
int64_t mdiv(int64_t *x, size_t n, int64_t y);

// To jest struktura przechowująca dane do testów i oczekiwane wyniki.
typedef struct {
  size_t    n; // rozmiar dzielnej
  int64_t  *x; // dzielna
  int64_t   y; // dzielnik
  int64_t  *z; // oczekiwany iloraz
  int64_t   r; // oczekiwana reszta
} test_data_t;


// if it returns 0x6969...6969 then ok
// pointer to alocated test, to see if bazgrać po rejestrach
// arguments are the same af for mdiv
// if returns 0x69...69 everything ok
// otherwise, go to gdb :(
int64_t abi_test(int64_t *x, size_t n, int64_t y);


int main() {
    // jakiś przykład po prostu
    test_data_t test = {2, (int64_t[2]){0, -13}, -5, (int64_t[2]){0x9999999999999999,  2}, -3};

    int64_t ret = abi_test(test.x, test.n, test.y);
    printf("Chciałem 0x%016" PRIx64"\nDostałem 0x%016"PRIx64"\n", zlota_liczb, ret);
    return (ret != zlota_liczb);
}
