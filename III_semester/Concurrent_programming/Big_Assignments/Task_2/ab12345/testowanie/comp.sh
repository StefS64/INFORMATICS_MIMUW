#!/bin/bash

check=false
while getopts v:* flag
do
    case "${flag}" in
        v) check=$OPTARG;;
    esac
done


clang++ -Wall -Wextra -O2 test_gen.cpp -o gen.e
if [ $? != 0 ]; then
	echo -ne "Kompilacja rozwiazania wzorcowego nie powiodla sie.\n"
    exit -1
fi

if [ "$check" = true ]; then
    echo -ne "Testowanie przy uzyciu valgrinda.\n"
else
    echo -ne "Testowanie bez valgrinda.\n"
fi

for i in {00000..99999}
do
	echo $i > seed
	./gen.e < seed > wej.in
    ../build/reference/reference < wej.in > brut.out
	if [ "$check" = true ]; then
    	valgrind -q --exit-on-first-error=yes --error-exitcode=-3 --log-file=memcheck.log --tool=memcheck --leak-check=yes ../build/nonrecursive/nonrecursive < wej.in > wzor.out
		if [ $? != 0 ]; then
			echo -ne "$i NIEOK"
			break
		fi
    else
        ../build/nonrecursive/nonrecursive < wej.in > wzor.out
    fi
#/wzor.e < wej.in > wzor.out
#	./brut.e < wej.in > brut.out
    # Compare only the first lines of the output files
    head -n 1 wzor.out > wzor_first_line.out
    head -n 1 brut.out > brut_first_line.out

	if diff -b wzor_first_line.out brut_first_line.out > /dev/null
	then
		echo -ne "\r$i OK"
	else
		echo -ne "\r$i NIEOK"
		break
	fi
done
echo -ne "\n"