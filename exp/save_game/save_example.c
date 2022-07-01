#include <stdio.h>
#include <gb/gb.h>
UBYTE *RAMPtr;



int main()
{
    ENABLE_RAM_MBC1;
    RAMPtr = (UBYTE *)0xa000;
    printf("Hex number -> 0x%x", RAMPtr[0]);
    RAMPtr[0]++;
    DISABLE_RAM_MBC1;
    return(0);
}