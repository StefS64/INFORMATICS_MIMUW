#include <assert.h>
#include <inttypes.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZE(x) (sizeof x / sizeof x[0])

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


int main() {
    size_t test_num = 69;
    int64_t a = 0;
    size_t n = 0;

    // IO format:
    // test num, n
    // dividend
    // divisor
    // quotient
    // remainder
    scanf("%lld %lld", &test_num, &n);
    test_data_t test = {n, NULL, 0, NULL, 0};
    test.x = (int64_t *) malloc(n * sizeof(int64_t));
    test.z = (int64_t *) malloc(n * sizeof(int64_t));
    assert(test.x != NULL && test.z != NULL); // c moment

    for (int i=0;i<n;i++) {
        scanf("%lld", test.x + i);
    }
    scanf("%lld", &test.y);
    for (int i=0;i<n;i++) {
        scanf("%lld", test.z + i);
    }
    scanf("%lld", &test.r);

    int64_t r = mdiv(test.x, n, test.y);

    bool pass = true;
    if (r != test.r) {
      pass = false;
      printf("W teście %zu reszta\n"
             "jest        %" PRIi64 ",\n"
             "powinna być %" PRIi64 ".\n",
             test_num, r, test.r);
    }
    for (size_t i = 0; i < n; ++i) {
      if (test.x[i] != test.z[i]) {
        pass = false;
        printf("W teście %zu w ilorazie pod indeksem %zu\n"
               "jest        0x%016" PRIx64 ",\n"
               "powinno być 0x%016" PRIx64 ".\n",
            //    "jest        0b%064" "b" ",\n"
            //    "powinno być 0b%064" "b" ".\n",
               test_num, i, test.x[i], test.z[i]);
      }
    }
    free(test.x); free(test.z);

    if (pass) {
      printf("Test %zu zakończył się poprawnie.\n", test_num);
    }

    return !pass;
}
