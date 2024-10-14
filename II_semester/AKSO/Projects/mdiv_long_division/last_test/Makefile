
.PHONY: all clean

abi_asm.o: abi.asm
	nasm -f elf64 -w+all -w+error -o $@ $<

mdiv_asm.o: mdiv.asm
	nasm -f elf64 -w+all -w+error -o $@ $<


mdiv_example.o: mdiv_example.c
	gcc -c -Wall -Wextra -std=c17 -O2 -g -o $@ $<

abi_test.o: abi_test.c
	gcc -c -Wall -Wextra -std=c17 -O2 -g -o $@ $<

# *.o: *.c
# 	gcc -c -Wall -Wextra -std=c17 -O2 -g -o $@ $<

tester_abi: abi_asm.o mdiv_asm.o abi_test.o
	gcc -z noexecstack -g -o $@ $^

tester: mdiv_asm.o mdiv_example.o
	gcc -z noexecstack -g -o $@ $^
	
#  objdump -d -M intel-mnemonic mdiv.o --visualize-jumps=color
all: tester
clean:
	rm *.o *.so *.i *.s *.a *.ii