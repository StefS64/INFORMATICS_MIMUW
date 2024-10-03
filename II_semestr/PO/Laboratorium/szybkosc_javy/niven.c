#include <stdio.h>
#include <stdbool.h>

int sum_of_digits(int num){
  int sum = 0;
  while(num > 0){
    sum += num % 10;
    num/=10;
  }
  return sum;
}



bool is_niven(int num){
  return (num % sum_of_digits(num) == 0);
}

int nth_niven(int n){
  int count = 0;
  int answer = 0;
  while(count < n) {
    answer++;
    if (is_niven(answer)) {
       count++;
    }
  }
  return answer;
}



int main(){
  int n = 1000000000;
  printf("%d", nth_niven(n));

}
