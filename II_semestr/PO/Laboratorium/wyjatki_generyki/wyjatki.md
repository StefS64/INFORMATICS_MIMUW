



## Wyjątki 

Jest wiele sytuacji gdy chcemy powidomić o błedach w programie.


Te wyjątki dzielą się na dwie klasy:
### Error
	Podchodzimy do tego incaczej niż w innych jezykach 
	Są to najcięższe przypadki które kończą działanie programu.
	Przykładowo:
	- Out of memoery error 



### Exception
	
	Służą zasygnalizowaniu błedu jakiejś niestandardowej sytuacji 


	Przykładowo:
	- Urzytkownik podał niestandardowy input


	Dzielą się na dwa rodzaje:
Runtime exception( podklasa exception ) jeżeli jest podklasą teg oto jest unchecked reszta jest checked
#### Unchecked





#### Checked



	















# Praktyka:
UWAGA można rzucić tylko coś co jest: Throwable 
Exception i Error dziedziczą po Throwable



## Jak tworzyć:
var exception = new IllegalStateException("BOOM")
throw exception


IOException
throw exception - będzie nie zadaowolony
bo nie throwujemy z całej funkcji


Zostanie wyepłany stack trace będzie wudocznu stos wywołań 


Dlaej możmy robić try{
	
	throw exception
}catch (Throwable t ){
	t.printStackTrace().
}


Mozna oczywiście zawężać wyjątki do niektórych 

catch (IOExce... | RunTime...){
	- tu z wspólnych method możem y korzystać 
}


ex.fillInStackTrace().










#typy generyczne:


Dwa głowne podejścia:




