PROGRAMS = hello print last_param_len execve

.PHONY: all clean

all: $(PROGRAMS)

print last_param: macro_print.asm

%: %.asm
	nasm -f elf64 -w+all -w+error -o $@.o $<
	ld --fatal-warnings -o $@ $@.o

clean:
	rm -rf $(PROGRAMS) *.o
