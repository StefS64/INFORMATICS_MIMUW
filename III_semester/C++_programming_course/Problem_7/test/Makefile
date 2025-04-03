GCC = clang++
CPPFLAGS = -Wall -Wextra -Werror-limit=1 -std=c++23 -O2
TARGET = program
SOURCES = $(wildcard *.cpp)

all: clean $(TARGET)

$(TARGET): $(SOURCES)
	$(GCC) $(CPPFLAGS) -o $@ $^

clean:
	rm -f $(TARGET)
