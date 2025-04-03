// 0 < MIN < ludzie <= MAX
turysta(){
    sekcja_lok;
    int goral = MOKO.czeka_na_woz();
    jedź();
    MOKO.wysiadam(goral);
}


goral(int id){
    sekcja_lok;
    MOKO.czekam_na_turystow(int id);
    jedź();
    MOKO.czekam_na_wysiadajacych();
}


MOKO {
    condition czekam_woz;
    condition gorale
    condition wysiad[K];
    bool goral_laduje = false;
    int ile_czeka = 0;
    int ile_zaldowano[K] = {0,...};
    int akt_goral = 0;



    int czeka_na_wóz(){
        ile_czeka++;
        if (ile_czeka > MAX || empty(gorale) || ile_czeka < MIN) {
            wait(czekam_woz);
        } else {
            signal(gorale)
        }
        ile_czeka--;
        ile_zaladowano[aktgoral]++;
        int goral = aktgoral;
        if (ile_zaladowano[aktgoral] < MAX && ile_czeka > 0) {
            signal(czekam_woz);
        } else {
            goral_laduje = false;
            // if (ile_czeka >= MIN){
            //     signal(gorale);
            // }
        }
        return goral;
    }

    void wysiadam(int goral) {
        ile_zaladowano[goral]--;
        if(ile_zalad[goral] == 0) {
            signal(wysiad[goral]);
        }
    }

    void czekam_na_turstow(int id) {
        if (goral_laduje || ile_czeka < MIN) {
            wait(gorale);
        } else if (ile_czeka >= MIN) {
            signal(czekam_woz)
        }
        akt_gorale = id;
        goral_laduje = true;
    }

    void czekam_na_wysiadających(int id) {
        if (ile_zaladowano[id] > 0) {
            wait(wysiad[id]);
        }
    }
}