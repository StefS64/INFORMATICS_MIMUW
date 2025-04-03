Binary semaphore mutex = 1;
Semaphore czytelnik = 0;
Semaphore pisarz = 0;
int czekapis = 0;
int ilepis = 0;
int ileczyta = 0;
int ileGrupCzeka = 0;
int ile_czeka_czyt = 0;
int ile_wypuszczam = 0;
int niepelna_grupa =0;


Proces P(int grupa) {
    while(true) {
        wlasne_sprawy();
        P(mutex)
        if(grupa == 1) {//czytelnik
            if (ilepis + czekapis > 0) {
                ile_czeka_czyt++;
                niepelna_grupa++;
                if (niepelna_grupa == K) {
                    niepelna_grupa = 0;
                    ile_grup_czeka++;
                }
                V(mutex);
                P(czytelnik);
            } else {
                niepelna_grupa++;
                if (niepelna_grupa == K) {
                    niepelna_grupa = 0;
                    ile_grup_czeka++;
                } else {
                    V(mutex);
                    P(czytelnik);
                }
            } // Tutaj mamy x grup i chcemy je wpuscic l maruderów chce wpusici tylo x*K
            if (ile_grup_czeka > 0){
                ile_wypuszczam++;
                ileczyta++;
                if (ile_wypuszczam == K) {
                    ile_wypuszczam = 0;
                    ile_grup_czeka--;
                }
                V(czytelnik);
            } else {
                V(mutex);
            }
        } else {//pisarz
            if(ileczyta > 0) {
                czekapis++;
                V(mutex)
                P(pisarz);
            }
            ile_pisze++;
            V(mutex)
        }
        Korzystam();
        P(mutex);
        if(grupa == 1) { //czytelnik
            ileczyta--;
            if (ileczyta == 0 && czeka_pis > 0) {
                V(pisarze);
            } else if (ile_czyta == 0) {
                V(czytelnik);
            } else {
                V(mutex);
            }
        } else { // pisarz
            ile_pisze--;
            if(ile_grup_czeka > 0) {
                V(czytelnik);
            } else if (czekapis > 0) {
                V(pisarze);
            } else {
                V(mutex);
            }
        }
    }
}
