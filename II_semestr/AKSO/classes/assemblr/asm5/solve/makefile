PROGRAMS = heap memory

.PHONY: all clean

all: $(PROGRAMS)

%: %.asm
	nasm -f elf64 -w+all -w+error -I../../asembler-4/rozwiazania -o $@.o $<
	ld --fatal-warnings -o $@ $@.o

clean:
	rm -rf $(PROGRAMS) *.o
