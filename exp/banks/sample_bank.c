#include <gb/gb.h>
#include <stdio.h>

#include "./structdef.c"

extern int numbers;
extern char * inrom;
extern struct CanIBeConst lol2;
struct CanIBeConst * ptr = &lol2;

void main() 
{
    SWITCH_ROM_MBC1(3);
    delay(1000);
    printf("here's num: %d", ptr->number);
    delay(1000);
    SWITCH_ROM_MBC1(2);
    printf("here's num: %d", ptr->number);
    delay(1000);
}