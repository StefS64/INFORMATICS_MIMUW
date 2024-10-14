#include "wys.h"
#include <iostream>
#include <vector>
#include <map> 
using namespace std; 
  
map<vector<int>, pair<int, int>> mapa;//trzyma ilość potrzebnych kroków do zakończenia oraz następny krok w zależności od stanu wektora

int n, k, g;//początkowe zmienne globalne

bool ten_sam_vector(vector<int> &a, vector<int> &b){
   for(int i = 0; i < (int)a.size(); i++){
      if(a[i] != b[i]){
         return 0;
      }
   }
   return 1;
}

/*
Tworzy nowy wektor, który ma zwiekszone komórki, które wymagałyby dodatkowego kłamstwa o jeden.
Pola z większą niż maksymalną ilością kłamstw pozostawia z wartością k + 1. 
*/

vector<int> nowy_zaktualizowany_wektor(vector<int> &aktualny_stan, int zap, bool odpowiedz){
   vector<int> nowy = aktualny_stan;
   for(int i = 0; i < n; i++){
      if(zap - 1 > i && !odpowiedz){
         nowy[i]++;
      }
      if(zap - 1 <= i && odpowiedz){
         nowy[i]++;
      }
      if(nowy[i] > k + 1){
         nowy[i] = k + 1;
      }
   }
   return nowy;
}

/*
   Funkcja pre_proces zwraca minimalną liczbę ruchów by mieć pewność odgadnięcia odpowiedzi z aktualnego stanu wektora(komórka vecotra zawiera ilość kłamstw których Ala musiałaby powiedzieć by nie być pewien czy ta liczba jest jedną z możliwości dobrego wyniku) 
   Przy okazji wrzuca najbardziej optymalny ruch z stanu aktualnego do mapy.
*/

int pre_proces(vector<int> &aktualny_stan, int zap, bool odpowiedz){//stan wektora indeks zapytania oraz odpowiedz twierdzaca lub nie
   vector<int> nowy = nowy_zaktualizowany_wektor(aktualny_stan, zap, odpowiedz);
   if(ten_sam_vector(nowy, aktualny_stan) && zap){
      return 100000;
   }
   if(mapa.count(nowy)){
      return mapa[nowy].first;
   }
   int ile_nie_pelne = 0, indeks_szukany = 0;
   for(int i = 0; i < n && ile_nie_pelne < 2; i++){
      if(nowy[i] != k + 1){
         ile_nie_pelne++;
         indeks_szukany = i + 1;
      }
   }
   if(ile_nie_pelne == 1){
      mapa.insert(pair<vector<int>, pair<int, int>>(nowy, make_pair(0, indeks_szukany)));
      return 0;
   }
   int minmalna_liczba_ruchow = 10000;
   int ruch = -1;
   int porownaj = 10000;//zapamieta najmniejszą wartość z rozpatrywanych przypadków
   for(int i =  2; i <= n; i++){
      porownaj = max(pre_proces(nowy, i, 0), pre_proces(nowy, i, 1));
      if(porownaj < minmalna_liczba_ruchow){
         ruch = i;
         minmalna_liczba_ruchow = porownaj;
      }
   }
   mapa.insert(pair<vector<int>, pair<int, int>>(nowy, make_pair(minmalna_liczba_ruchow + 1, ruch)));
   return minmalna_liczba_ruchow + 1;
}

void zagraj(vector<int> stan){
   if(mapa[stan].first == 0){
      odpowiedz(mapa[stan].second);
   }
   else{
      if(mniejszaNiz(mapa[stan].second)){
         zagraj(nowy_zaktualizowany_wektor(stan, mapa[stan].second, true));
      }
      else{
         zagraj(nowy_zaktualizowany_wektor(stan, mapa[stan].second, false));
      }
   }
}

int main() {
   dajParametry(n, k, g);
   vector<int> wektor_poczatkowy(n, 0);
   pre_proces(wektor_poczatkowy, 0, false);
   while(g--){
      zagraj(vector<int>(n, 0));
   }
}