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
CMAKE_SOURCE_DIR = /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build

# Include any dependencies generated for this target.
include CMakeFiles/get_memory.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/get_memory.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/get_memory.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/get_memory.dir/flags.make

CMakeFiles/get_memory.dir/get_memory.c.o: CMakeFiles/get_memory.dir/flags.make
CMakeFiles/get_memory.dir/get_memory.c.o: ../get_memory.c
CMakeFiles/get_memory.dir/get_memory.c.o: CMakeFiles/get_memory.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/get_memory.dir/get_memory.c.o"
	/usr/bin/gcc-11 $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/get_memory.dir/get_memory.c.o -MF CMakeFiles/get_memory.dir/get_memory.c.o.d -o CMakeFiles/get_memory.dir/get_memory.c.o -c /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/get_memory.c

CMakeFiles/get_memory.dir/get_memory.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/get_memory.dir/get_memory.c.i"
	/usr/bin/gcc-11 $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/get_memory.c > CMakeFiles/get_memory.dir/get_memory.c.i

CMakeFiles/get_memory.dir/get_memory.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/get_memory.dir/get_memory.c.s"
	/usr/bin/gcc-11 $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/get_memory.c -o CMakeFiles/get_memory.dir/get_memory.c.s

# Object files for target get_memory
get_memory_OBJECTS = \
"CMakeFiles/get_memory.dir/get_memory.c.o"

# External object files for target get_memory
get_memory_EXTERNAL_OBJECTS =

get_memory: CMakeFiles/get_memory.dir/get_memory.c.o
get_memory: CMakeFiles/get_memory.dir/build.make
get_memory: liberr.a
get_memory: CMakeFiles/get_memory.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable get_memory"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/get_memory.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/get_memory.dir/build: get_memory
.PHONY : CMakeFiles/get_memory.dir/build

CMakeFiles/get_memory.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/get_memory.dir/cmake_clean.cmake
.PHONY : CMakeFiles/get_memory.dir/clean

CMakeFiles/get_memory.dir/depend:
	cd /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11 /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11 /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build/CMakeFiles/get_memory.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/get_memory.dir/depend

