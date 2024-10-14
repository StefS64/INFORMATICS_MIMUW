PROGRAMS = print last_param brk

.PHONY: all clean

all: $(PROGRAMS)

print brk: macro_print.asm

%: %.asm
	nasm -f elf64 -w+all -w+error -o $@.o $<
	ld --fatal-warnings -o $@ $@.o

clean:
	rm -rf $(PROGRAMS) *.o

