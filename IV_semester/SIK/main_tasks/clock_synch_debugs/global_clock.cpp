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

uint64_t GlobalClock::getT3(){
  return T[3];
}

void GlobalClock::saveT(size_t index, uint64_t time) {
  index--;
  if (index < 4) {
    T[index] = time;
  }
}

void GlobalClock::calcOffset() {
  uint64_t sum1 = T[1] + T[2];
  uint64_t sum2 = T[3] + T[0];

  if (sum1 >= sum2) {
    offset = (sum1 - sum2) / 2;
  } else {
    offset = UINT64_MAX - (sum2 - sum1)/2;
  }
  offset = offset % UINT64_MAX;
}

uint64_t GlobalClock::getOffset() {
  return offset;
}

void GlobalClock::resetOffset() {
  for (int i = 0; i < 4; i++) {
    T[i] = 0;
  }
  offset = 0;
}
