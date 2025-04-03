// Wagi
void Zadanie();

binary semaphore mutex = 1;
int wagi[2] = {0,0};
bool wystarczy[2] = {false, false};
binary semaphore czeka[typ] = 0;
int sum_obudz[2] = {0,0};
bool czy_pierwsz = true;





Process P(bool typ, int waga){
    while(true) {
        P(mutex);
        wagi[typ] += waga;
        if (wagi[0] < 2K || wagi[1] < 2K) {
            V(mutex);
            P(czeka[typ]);
        } else {
            sum_obudz[typ] = 0;
            sum_obudz[typ^1] = 0;
            czy_pierwsza = true;
        }

        wagi[typ] -= waga;
        sum_obudz[typ] += waga;

        if (sum_obudz[typ] < 2K) {
            V(czeka[typ]);
        } else if(czy_pierwsz){
            czy_pierwsza = false;
            V(czeka[typ^1]);
        } else {
            V(mutex);
        }
        
        zadanie();
    }
}