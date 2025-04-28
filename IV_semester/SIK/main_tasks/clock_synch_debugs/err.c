#include <errno.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "err.h"
#define RESET   "\x1b[0m"
#define RED     "\x1b[31m"
#define GREEN   "\x1b[32m"
#define YELLOW  "\x1b[33m"
#define BLUE    "\x1b[34m"

void syserr(const char* fmt, ...) {
    va_list fmt_args;
    int org_errno = errno;

    fprintf(stderr, RED "\tERROR " RESET);

    va_start(fmt_args, fmt);
    vfprintf(stderr, fmt, fmt_args);
    va_end(fmt_args);

    fprintf(stderr, " (%d; %s)\n", org_errno, strerror(org_errno));
    exit(1);
}

void fatal(const char* fmt, ...) {
    va_list fmt_args;

    fprintf(stderr, RED "\tERROR " RESET);

    va_start(fmt_args, fmt);
    vfprintf(stderr, fmt, fmt_args);
    va_end(fmt_args);

    fprintf(stderr, "\n");
    exit(1);
}

void error(const char* fmt, ...) {
    va_list fmt_args;
    int org_errno = errno;

    fprintf(stderr, RED "\tERROR " RESET);

    va_start(fmt_args, fmt);
    vfprintf(stderr, fmt, fmt_args);
    va_end(fmt_args);

    if (org_errno != 0) {
      fprintf(stderr, " (%d; %s)", org_errno, strerror(org_errno));
    }
    fprintf(stderr, "\n");
}

void error_msg(const void* buf, size_t len) {
    fprintf(stderr, RED "ERROR MSG " RESET);
    const unsigned char* bytes = (const unsigned char*)buf;
    size_t to_print = len > 10 ? 10 : len;
    for (size_t i = 0; i < to_print; ++i) {
        fprintf(stderr, "%02x", bytes[i]);
    }
    fprintf(stderr, "\n");
}
