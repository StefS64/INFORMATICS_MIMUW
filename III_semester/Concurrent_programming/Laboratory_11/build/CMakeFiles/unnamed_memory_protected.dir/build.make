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
include CMakeFiles/unnamed_memory_protected.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/unnamed_memory_protected.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/unnamed_memory_protected.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/unnamed_memory_protected.dir/flags.make

CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o: CMakeFiles/unnamed_memory_protected.dir/flags.make
CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o: ../unnamed_memory_protected.c
CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o: CMakeFiles/unnamed_memory_protected.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o"
	/usr/bin/gcc-11 $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o -MF CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o.d -o CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o -c /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/unnamed_memory_protected.c

CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.i"
	/usr/bin/gcc-11 $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/unnamed_memory_protected.c > CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.i

CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.s"
	/usr/bin/gcc-11 $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/unnamed_memory_protected.c -o CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.s

# Object files for target unnamed_memory_protected
unnamed_memory_protected_OBJECTS = \
"CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o"

# External object files for target unnamed_memory_protected
unnamed_memory_protected_EXTERNAL_OBJECTS =

unnamed_memory_protected: CMakeFiles/unnamed_memory_protected.dir/unnamed_memory_protected.c.o
unnamed_memory_protected: CMakeFiles/unnamed_memory_protected.dir/build.make
unnamed_memory_protected: liberr.a
unnamed_memory_protected: CMakeFiles/unnamed_memory_protected.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable unnamed_memory_protected"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/unnamed_memory_protected.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/unnamed_memory_protected.dir/build: unnamed_memory_protected
.PHONY : CMakeFiles/unnamed_memory_protected.dir/build

CMakeFiles/unnamed_memory_protected.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/unnamed_memory_protected.dir/cmake_clean.cmake
.PHONY : CMakeFiles/unnamed_memory_protected.dir/clean

CMakeFiles/unnamed_memory_protected.dir/depend:
	cd /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11 /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11 /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build /home/st0ic/Desktop/MIMUW/Informatics/III_semester/Concurrent_programming/Laboratory_11/build/CMakeFiles/unnamed_memory_protected.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/unnamed_memory_protected.dir/depend

