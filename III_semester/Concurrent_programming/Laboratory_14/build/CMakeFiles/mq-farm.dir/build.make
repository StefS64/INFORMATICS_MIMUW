# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.22

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build

# Include any dependencies generated for this target.
include CMakeFiles/mq-farm.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/mq-farm.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/mq-farm.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/mq-farm.dir/flags.make

CMakeFiles/mq-farm.dir/mq-farm.c.o: CMakeFiles/mq-farm.dir/flags.make
CMakeFiles/mq-farm.dir/mq-farm.c.o: ../mq-farm.c
CMakeFiles/mq-farm.dir/mq-farm.c.o: CMakeFiles/mq-farm.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/mq-farm.dir/mq-farm.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/mq-farm.dir/mq-farm.c.o -MF CMakeFiles/mq-farm.dir/mq-farm.c.o.d -o CMakeFiles/mq-farm.dir/mq-farm.c.o -c /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/mq-farm.c

CMakeFiles/mq-farm.dir/mq-farm.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/mq-farm.dir/mq-farm.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/mq-farm.c > CMakeFiles/mq-farm.dir/mq-farm.c.i

CMakeFiles/mq-farm.dir/mq-farm.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/mq-farm.dir/mq-farm.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/mq-farm.c -o CMakeFiles/mq-farm.dir/mq-farm.c.s

# Object files for target mq-farm
mq__farm_OBJECTS = \
"CMakeFiles/mq-farm.dir/mq-farm.c.o"

# External object files for target mq-farm
mq__farm_EXTERNAL_OBJECTS =

mq-farm: CMakeFiles/mq-farm.dir/mq-farm.c.o
mq-farm: CMakeFiles/mq-farm.dir/build.make
mq-farm: CMakeFiles/mq-farm.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable mq-farm"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/mq-farm.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/mq-farm.dir/build: mq-farm
.PHONY : CMakeFiles/mq-farm.dir/build

CMakeFiles/mq-farm.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/mq-farm.dir/cmake_clean.cmake
.PHONY : CMakeFiles/mq-farm.dir/clean

CMakeFiles/mq-farm.dir/depend:
	cd /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14 /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14 /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_14/build/CMakeFiles/mq-farm.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/mq-farm.dir/depend

