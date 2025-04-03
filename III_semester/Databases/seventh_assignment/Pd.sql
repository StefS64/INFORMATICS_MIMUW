CREATE TABLE Mieszkaniec (

    pesel VARCHAR(11) PRIMARY KEY,
    
	nazwisko VARCHAR(50) NOT NULL,

    imie VARCHAR(20) NOT NULL,

    adres VARCHAR(100)

);

CREATE TABLE Mieszkanie (

    adres VARCHAR(100) PRIMARY KEY,

    PESEL_wlasciciela VARCHAR(11) NOT NULL FOREIGN KEY REFERENCES Mieszkaniec(pesel),

    metraz NUMBER(3) NOT NULL,

);


-- 

Założenie, że każde mieszkanie posiada jednego właściciela gwarantuje nam brak utraty zależności funkcyjnych

Bez niego stracilibyśmy:
Adres -> Imię_właściciela, Nazwisko_właściciela, Adres_właściciela

PESEL_właściciela -> Imię właściciela, Nazwisko_właściciela, Adres
