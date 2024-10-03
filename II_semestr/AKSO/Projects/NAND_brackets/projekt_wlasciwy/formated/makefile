CC = gcc
CFLAGS = -Wall -Wextra -Wno-implicit-fallthrough -std=gnu17 -fPIC -O2
LDFLAGS = -shared -Wl,--wrap=malloc -Wl,--wrap=calloc -Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free -Wl,--wrap=strdup -Wl,--wrap=strndup

.PHONY: all clean

all: libnand.so

libnand.so: nand.o vector.o memory_tests.o
	$(CC) $(LDFLAGS) -o $@ $^

testy: testy.o libnand.so
	$(CC)  -o $@ $< -L. -lnand

testy.o: testy.c nand.h
	$(CC) $(CFLAGS) -c -o $@ $<


nand.o:	nand.c nand.h vector.h
	$(CC) $(CFLAGS) -c -o $@ $<

vector.o: vector.c vector.h nand.h
	$(CC) $(CFLAGS) -c -o $@ $<

memory_tests.o: memory_tests.c memory_tests.h
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f *.o libnand.so

