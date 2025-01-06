add_executable(parallel main.c stack.c)
target_link_libraries(parallel io err atomic)
