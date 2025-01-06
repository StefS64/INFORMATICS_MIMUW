// autor: Stefan Świerczewski
#include <bits/stdc++.h>
using namespace std;
#define rep(a,b) for(int a=0;a<(b);a++)

int p(long long a, long long b){
	return a + (2*(long long)rand() + (long long)rand()%2) % (b-a+1);
}

const int MAX_THREAD = 1; 
const int MIN_BOUND = 3; 
const int MAX_BOUND = 7; // 50
const int MAX_NUM = 4; // 100 Bound for number of elements in multiset.
const int MIN_NUM = 0;


int main()
{
    ios_base::sync_with_stdio(0); cin.tie(0);
	int seed;
	cin >> seed;
	srand(seed);

	int t = p(1, MAX_THREAD); // liczba wątków
	int d = p(MIN_BOUND, MAX_BOUND);
	int n = p(MIN_NUM, MAX_NUM);
	int m = p(MIN_NUM, MAX_NUM);
	cout << t <<" " << d << " " << n << " " << m << '\n';
	for (int i = 0; i < n; i++) {
		cout << p(1, d) << " ";
	}
	cout << '\n';
	for (int i = 0; i < m; i++) {
		cout << p(1, d) << " ";
	}
	cout << '\n';
	return 0;
}