#include <iostream>
#include <algorithm>

#include "wys.h"

namespace {
int _n, _k, _g;
int _x;
int ileGier, ileZapytan, maxZapytan;
}
int licznik;
void dajParametry(int &n, int &k, int &g) {
  std::cin >> _n >> _k >> _g;
  n = _n; k = _k; g = _g;
  _x = rand() % n + 1;
  licznik = k;
  ileGier = ileZapytan = maxZapytan = 0;
  std::cout << "n = " << _n << ", k = " << _k << ", g = " << _g << '\n';
}

bool mniejszaNiz(int y) {
  ileZapytan++;
  int x = rand() % (12);
  if (x < _k && licznik > 0){
    licznik--;
    return !(_x < y);
  }
  else
    return _x < y;
}

void odpowiedz(int x) {
  if (x != _x) {
    std::cout << "ZLE. Bledna odpowiedz w grze #" << ileGier << ": oczekiwano " << _x << " a uzyskano odpowiedz " << x << '\n';
    exit(1);
  }
  maxZapytan = std::max(maxZapytan, ileZapytan);
  ++ileGier;
  if (ileGier == _g) {
    std::cout << "OK. Zadano maksymalnie " << maxZapytan << " zapytan.\n";
    exit(0);
  }
  _x = rand() % _n + 1;
  ileZapytan = 0;
}
