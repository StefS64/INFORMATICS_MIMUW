CC       = gcc
CPPFLAGS = -Wall -O3 -pedantic -static
CFLAGS   = -std=c17 -pedantic -Wall -Wextra -Wformat-security


.PHONY: all clean 

generator.o: generator.c generator.h
input-output.o: input-output.c input-output.h
main.o: main.c input-output.h generator.h

libbyone.a: 
	ar -rcs $@ $^


new_linked.a: generetor.o input-output.o main.o
	$(CC) $(CFLAGS) -o $@




clean:
	rm -rf *.a


