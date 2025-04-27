#ifndef GLOBALCLOCK_HPP
#define GLOBALCLOCK_HPP
#include <chrono>
#include <algorithm> 
#include <iostream> // debug

class GlobalClock {
public:
  static void initialize();
  static uint64_t now();
  static void saveT(size_t index);
  static void saveT(size_t index, uint64_t time);
  static void calcOffset();
  static uint64_t getOffset();
  static void resetOffset();
  static uint64_t getT3();
  static void print_times() {// DEBUG TODO
    std::cout <<
      "offset" << offset <<
      "\nT1 "   << T[0] <<
      "\nT2 "   << T[1] <<
      "\nT3 "   << T[2] <<
      "\nT4 "   << T[3] << std::endl;
  }
private:
  static std::chrono::steady_clock::time_point start_time;
  static uint64_t offset;
  static uint64_t T[4];
};

#endif
