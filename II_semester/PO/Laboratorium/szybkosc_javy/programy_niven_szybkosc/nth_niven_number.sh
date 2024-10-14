#!/bin/bash

is_niven(){
	local number=$1
	local sumOfDigits=0
	temp=$number
	while((temp > 0)); do
		sumOfDigits=$((sumOfDigits + temp % 10))
		temp=$((temp / 10))
	done

	if((number % sumOfDigits == 0)); then 
		return 0
	else 
		return 1
	fi
}

nth_niven(){
	local n=$1
	local counter=0
	local num=0
	while((counter < n)); do
		((num++))
		if is_niven "$num"; then
			((counter++))
		fi
	done
	echo "$num"
}


input=$1
echo "$(nth_niven $input)"
