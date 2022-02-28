#pragma bank 3

#include "./structdef.c"

const int numbers = 10;
const char * inrom = "hello I am in rom don't forget I am in rom";

const struct CanIBeConst lol = {5, 0x00};
const struct CanIBeConst lol2 = {12, &lol};

// gbjam_9 % $LCC_BIN -Wl-yt2 -Wl-yo4 sample_bank.c bank_2.c -o test.gb 