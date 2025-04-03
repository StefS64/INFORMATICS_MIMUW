cmake_minimum_required (VERSION 3.1)
project (MIMUW-PW-lab13 C)

find_package (Threads REQUIRED)

# Set the C17 standard
set(CMAKE_C_STANDARD 17)
set(CMAKE_C_STANDARD_REQUIRED ON)

# Compiler flags for GCC targeting C17
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -O0 -Wall -std=c17 -latomic")

# http://stackoverflow.com/questions/10555706/
macro (add_executable _name)
    # invoke built-in add_executable
    _add_executable(${ARGV})
    if (TARGET ${_name})
        target_link_libraries(${_name} ${CMAKE_THREAD_LIBS_INIT} )
    endif()
endmacro()

add_executable(create create.c)
add_executable(send-receive send-receive.c)
add_executable(priorities priorities.c)
add_executable(async async.c)
add_executable(mq-farm mq-farm.c)

install(TARGETS DESTINATION .)
