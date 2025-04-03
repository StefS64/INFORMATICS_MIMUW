    // WYCIECZKI GĘSIEGO
potrzebnych: 2 <= MIN < osób <= MAX oraz przewodnik

proces Turysta() {
		while(true){
        własne_sprawy();
        (int, int, int) (przewodnik, przede_mną, za_mną) = Wycieczka.turysta_zgłoś_się(id);
        wycieczka(przewodnik, przede_mną, za_mną);
        Wycieczka.turysta_skończył();
 	 }
}



process Przewodnik(){
	while(true){
  	własne_sprawy();
  	int ile_osob = Wycieczka.przewodnik_zgłoś_się();
    Wycieczka.prowadze_wycieczke(ile_osob);
    Wycieczka.koncze_wycieczke();
  }
}


Monitor Wycieczka {

  condition czekam_turysta;
  condition czekam_przewodnik;
  condition zliczanie;
  int ile_czeka_turystow = 0;
  int jak_duza_grupa = 0;
  int poprzedni = 0;
  int aktprzew = 0;
  
  int T[3] turysta_zgłoś_się(int id) {
  		int przew, nastep = 0, poprzed = 0; // lokalne zmienne dla naszego turysty
      ile_czeka_turystow++;
      poprzed = poprzedni; // 1 <- 2 <- 3 <- 4 <- 5 <- 6 MAX//0 <- 7
      poprzedni = id;
      if(ile_czeka_turystow % MAX == 0){
      	poprzedni = 0;
      }
      
      if(empty(czekam_przewodnik) || ile_czeka_turystow < MIN || ile_czeka_turstow > MAX) {
          wait(czekam_turysta);
      } else {
      		nastep = 0;
      		signal(czekam_turysta);       
      }
      
      ile_czeka_turystow--;
      jak_duza_grupa++;
      
      if( jak_duza_grupa != MAX && ile_czeka_turstow != 0) {
				signal(czekam_turysta);
      } else {
      	signal(czekam_przewodnik);
      }
      nastep = poprzedni;
      poprzedni = id;
      
      przew = aktprzew;
      
      return przew, nastep, poprzed;
  }
  
	void turysta_skończył(){};
	

	void przewodnik_zgłoś_się(int id){
    if(ile_czek_turystow < MIN) {
    	wait(czekam_przewodnik);
    } else {
      aktprzew = id;
      signal(czekam_tursyta); 
    }
    aktprzew = id;
  	return jak_duza_grupa;
    }


	void koncze_wycieczke();
}
