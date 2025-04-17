# Choose your compilers
CC     = gcc
CXX    = g++

# Compilation flags
CFLAGS   = -Wall -O2
CXXFLAGS = -Wall -O2

# List of source files (C and C++)
C_SRCS   = common.c err.c
CPP_SRCS = node_config.cpp peer-time-sync.cpp network_manager.cpp

# Corresponding object files
C_OBJS   = $(C_SRCS:.c=.o)
CPP_OBJS = $(CPP_SRCS:.cpp=.o)

# Final executable name
TARGET   = peer-time-sync

# Default rule: build the program
all: $(TARGET)

# Link all object files into final executable
$(TARGET): $(C_OBJS) $(CPP_OBJS)
	$(CXX) $(C_OBJS) $(CPP_OBJS) -o $@

# Compile C sources into objects
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Compile C++ sources into objects
%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Explicit header dependencies:
# If node_config.cpp includes node_config.hpp and uses common.h:
node_config.o: node_config.hpp common.h err.h
# And if common.c (or any other file that uses common.h) requires err.h as well:
common.o: common.h err.h

# (Optional) If peer-time-sync.cpp also uses node_config.hpp or common.h:
peer-time-sync.o: node_config.hpp common.h network_manager.hpp

# "make clean" to remove object files and the binary
clean:
	rm -f $(C_OBJS) $(CPP_OBJS) $(TARGET)
remove_obj:
	rm -f $(C_OBJS) $(CPP_OBJS)
