CC = gcc
AS = nasm
CFLAGS = -std=gnu17 -Wall -O2 -pthread -latomic
ASFLAGS = -f elf64

all: semaphore_test

semaphore_test: semaphore_test.o custom_sem.o
	$(CC) $(CFLAGS) -o semaphore_test semaphore_test.o custom_sem.o

semaphore_test.o: semaphore_test.c custom_sem.h
	$(CC) $(CFLAGS) -c semaphore_test.c

custom_sem.o: custom_sem.asm
	$(AS) $(ASFLAGS) -o custom_sem.o custom_sem.asm

clean:
	rm -f semaphore_test semaphore_test.o custom_sem.o

.PHONY: all clean
