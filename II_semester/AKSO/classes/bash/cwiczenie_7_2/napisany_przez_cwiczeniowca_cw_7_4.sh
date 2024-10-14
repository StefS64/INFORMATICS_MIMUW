#!/bin/bash

if (($# == 0)); then
	set "."
fi

shopt -s nullglob # puste pliki

for dir in "$@"; do
	if [[ ! -d "$dir" ]]; then
		echo "to nie jest katalog: $dir"
		continue
	fi

	find -type d --exec basename '{}' ';'
	# uwaga gwiazdki nie maczują ukrytych plików ale zmaczonwane .* maczuje z dwoma i n kropkami nie maczuje pseudo plików
	for file in "$dir"/*; do
		if [[ -f "$file" && -s "$file" ]]; then	# uwaga można && flagi jak obok
			basename "$file"
			# echo "$file"
		fi
	done
	echo "$dir"
done 

