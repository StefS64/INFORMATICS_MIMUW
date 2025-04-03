//N > 0, K > 1

Zawodnik(int drużyna) {
    int przeciwnik = Mecz.czekam_na_druzyne();
    mecz(przeciwnik);
    Mecz.schodze(drużyna);
}


Monitor Mecz {
    condition NaBoisko;
    condition NaPare;
    condition reszta[N];
    condition na_koniec[N];
    condition kapitan[N];
    bool jest_kapitan[N] = {false, false, ...};
    int ile_gra[N] = {0, 0, ...};
    int przeciwnik[N];
    int zgłoszona = 0;
    int zgloszona = 0;
    bool PusteBoisko = true;


    int czekam_na_druzye(int dru) {
        if (!jest_kapitan[dru]) {
            ile_w_dru[dru]++;
            if (ile_w_dru[dru] == K) {
                signal(kapitan[dru]);
            }
            wait(reszta[dru]);
        } else {
            jest_kapitan[dru] = true;
            ile_w_dru[dru]++;
            wait(kapitan[dru]);
            if (empty(Napare)) {
                wait(NaPare);
            } else {
                zgloszona = dru;
                przeciwnik[dru] = zgloszona;
                przeciwnik[zgloszona] = dru;
                if(!PusteBoisko){
                    wait(NaBoisko);
                }
                signal(NaPare);
            }
        }

        ile_gra[dru]++;
        if(!empty(reszta[dru])){
            signal(reszta[dru])
        }
        return przeciwnik[dru];
    }


    void schodze(int dru) {
        ile_gra[dru]--;
        if (ile_gra[dru] == 0 && ile_gra[przeciwnik[dru]] == 0) {
            PusteBoisko = true;
            signal(NaBoisko);
        }
    }

}