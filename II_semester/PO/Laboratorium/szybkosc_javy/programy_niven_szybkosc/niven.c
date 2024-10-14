#include <stdio.h>
#include <stdbool.h>

long sum_of_digits(long num){
  long sum = 0;
  while(num > 0){
    sum += num % 10;
    num/=10;
  }
  return sum;
}



bool is_niven(long num){
  return (num % sum_of_digits(num) == 0);
}

long nth_niven(long n){
  long count = 0;
  long answer = 0;
  while(count < n) {
    answer++;
    if (is_niven(answer)) {
       count++;
    }
  }
  return answer;
}



long main(){
  long n = 1000000000;
  printf("%ld", nth_niven(n));

}
