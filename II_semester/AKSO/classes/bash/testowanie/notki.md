# Jak wypisać randomowe liczby do plików :


for i in $(seq 10); do echo $RANDOM; done > 1.in



for i in $(seq 10); do echo $i; done



for f in *.in; do fout=$(echo "$f" | cut -d. -f1).out; echo "$fout"; done
for f in *.in; do fout=(echo "$f" | cut -d. -f1); ./sort <"f" > "$fout"

Czy ostatnia się udała:

echo $?

sort -cn 1.out //sortuje plik odpowiednio mnumerycznie z flagą -cn
sort normalnie sortuje alfabetycznei 



sort -cn 1.out && echo posortowane || echo nieposortowana



cat <( echo aaa) = echo aaa | car




diff <(./sort_compiled 1.in) <(sort -n 1.in)
- przekierowanie polecenia można stworzyć wiele pipów naraz

diff <(./sort_compiled < 1.in) <(sort -n 1.in) && echo posortowan || echo nieposrtowane

