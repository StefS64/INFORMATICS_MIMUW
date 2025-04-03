Pracownik(int firma){
    sekcja_lok;
    int druk = Biuro.chceDruk(firma);
    drukuj(druk);
    Biuro.skoncz_drukowac(firma, druk);
}



Monitor Biuro {
    enum stan = {drukuje, czeka, nie ma}
    condition pierwszy;
    condition reszta_firmy[N];
    bool wolna_druk[K] = {true,...}
    int ile_wolnych = K;
    int ile_drukuje[K] = {0,...}
    int drukarka_firma[N] = {0,...}
    bool czeka_pierwszy[N] = {false,....}
    int ile_firm_czeka = 0;

    int chceDruk(int firma){
        int druk = 0;
        if(ile_firm_czeka > 0 || (ile_wolnych == 0 && stan[firma] != drukuje)){
            if(czeka_pierwszy[firma]){
                wait(reszta[firma]);
            } else {
                stan[firma] = czeka;
                ile_firm_czeka++;
                czeka_pierwszy[firma] = true;
                wait(pierwszy);
                czeka_pierwszy[firma] = false;
                ile_firm_czeka--;
            }
        }
        if (!empty(reszta_firmy[firma])){
            signal(reszta[firma]);
        }
        if(stan[firma] == drukuje){
            druk = drukarka_firma[firma];
        } else {
            while(!wolna_druk[druk]){
                druk = (druk+1)%N;
            }
            stan[firma] = drukuje;
            drukarka_firma[firma] = druk;
            ile_wolnych--;
        }
        ile_drukuje[druk]++;
        return druk;
    }

    void skoncz_drukowac(int firma, int druk){
        ile_drukuje[druk]--;
        if(ile_drukuje[druk] == 0){
            ile_wolnych ++;
            stan[firma] = czeka;
            wolna_druk[druk] = true;
            signal(pierwszy);
        }
    }
}