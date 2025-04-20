#ifndef GLOBALCLOCK_HPP
#define GLOBALCLOCK_HPP
#include <chrono>

class GlobalClock {
public:
    static void initialize();
    static uint64_t now();
    static void saveT(size_t index);
    static void saveT(size_t index, uint64_t time);
    static void calcOffset();
    static uint64_t getOffset();

private:
    static std::chrono::steady_clock::time_point start_time;
    static uint64_t offset;
    static uint64_t T[4];
};

#endif
