#!/bin/bash

MOD=1000000007


multiplyMatrices2x2() {
	local -n matrixA=$1
	local -n matrixB=$2
	temp0=$(((matrixA[0]*matrixB[0]%MOD + matrixA[1]*matrixB[2]%MOD)%MOD))
	temp1=$(((matrixA[0]*matrixB[1]%MOD + matrixA[1]*matrixB[3]%MOD)%MOD))
	temp2=$(((matrixA[2]*matrixB[0]%MOD + matrixA[3]*matrixB[2]%MOD)%MOD))
	temp3=$(((matrixA[2]*matrixB[1]%MOD + matrixA[3]*matrixB[3]%MOD)%MOD))
	matrixA[0]=$temp0
	matrixA[1]=$temp1
	matrixA[2]=$temp2
	matrixA[3]=$temp3
#	echo "test: ${matrixA[@]}"
} 


multiply() {
	local -n matrix_const=$1
#	echo "test: ${matrix_const[@]}"
	local -n answer=$2
	n=$3
	while ((n > 0)); do
		multiplyMatrices2x2 answer matrix_const
#		echo "test: ${matrix_const[@]}"
#		echo "test: ${matrix_const[@]}"
		((n--))
		echo "$n"
	done
}


matrix=(0 1 1 1)
ans=(1 0 0 1)

multiply matrix ans 10000000

echo "test: ${ans[@]}"

