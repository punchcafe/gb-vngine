#include <stdio.h>
#include <gb/gb.h>
UBYTE *RAMPtr;


const unsigned char correct_save_signature [] = {0x01, 0xf1, 0x33, 0xf0, 0x11};

#define SAVE_RAM_POINTER 0xa000

struct SaveConfiguration {
    unsigned char save_signature [5];
    const unsigned short max_games;
    const short saves;
};



unsigned short valid_save_data()
{
    struct SaveConfiguration * config = (struct SaveConfiguration *)SAVE_RAM_POINTER;
    for(int i = 0; i < 5; i++)
    {
        if(config->save_signature[i] != correct_save_signature[i])
        {
            printf(".c 00%x .", config->save_signature[i]);
            return 0;
        }
    }
    return 1;
}

void set_save_data()
{
    struct SaveConfiguration * config = (struct SaveConfiguration *)SAVE_RAM_POINTER;
    for(int i = 0; i < 5; i++)
    {
        config->save_signature[i] = correct_save_signature[i];
        printf(". 00%x .", config->save_signature[i]);
    }
}


int main()
{
    ENABLE_RAM_MBC1;
    printf("valid save? %d", valid_save_data());
    RAMPtr = (UBYTE *)0xa000;
    printf("Hex number -> 0x%x", RAMPtr[0]);
    //RAMPtr[0]++;
    set_save_data();
    DISABLE_RAM_MBC1;
    return(0);
}

// Directly after save config is file data. So SAVE_RAM_POUNTER + sizeof(struct SaveConfiguration)