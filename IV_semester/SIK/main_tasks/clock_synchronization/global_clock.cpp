#include "global_clock.hpp"

std::chrono::steady_clock::time_point GlobalClock::start_time;
uint64_t GlobalClock::T[4] = {0,0,0,0};
uint64_t GlobalClock::offset = 0;

void GlobalClock::initialize() {
  start_time = std::chrono::steady_clock::now();
}

uint64_t GlobalClock::now() {
  auto now = std::chrono::steady_clock::now();
  auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(now - start_time);
  return elapsed.count();
}

void GlobalClock::saveT(size_t index) {
  index--;
  if (index < 4) {
    T[index] = now();
  }
}

void GlobalClock::saveT(size_t index, uint64_t time) {
  index--;
  if (index < 4) {
    T[index] = time;
  }
}
void GlobalClock::calcOffset() {
  offset = ((T[1] - T[0]) + (T[3] - T[2])) / 2;
}

uint64_t GlobalClock::getOffset() {
  return offset;
}

