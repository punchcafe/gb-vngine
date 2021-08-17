#include "./text_renderer.c"
#include "./narrative_elements/background_element.c"
#include <gb/gb.h>
#include <stdio.h>

#ifndef RENDER_BACKGROUND_DEFINITION
#define RENDER_BACKGROUND_DEFINITION

#define BACKGROUND_WIDTH 20
#define BACKGROUND_HEIGHT 12
#define BACKGROUND_TILE_AREA BACKGROUND_HEIGHT * BACKGROUND_WIDTH
#define BACKGROUND_START_Y_INDEX 0
#define BACKGROUND_START_X_INDEX 0

void render_background(struct BackgroundElement * element)
{
    unsigned short updated_sign_off [BACKGROUND_TILE_AREA];
    for(int i = 0; i < BACKGROUND_TILE_AREA; i++)
    {
        unsigned char shifted_index = element->tile_assignements[i] + TEXT_PATTERNS_END_INDEX + 1;
        updated_sign_off[i] = shifted_index;
    }
    set_bkg_data(TEXT_PATTERNS_END_INDEX + 1, element->number_of_tiles, element->background_tiles);
    for(int x = 0; x < BACKGROUND_WIDTH; x++)
    {
        for(int y = 0; y <BACKGROUND_HEIGHT; y++)
        {
            unsigned char symbol [1]= {updated_sign_off[x + y*BACKGROUND_WIDTH]};
            set_bkg_tiles(x,
                    y,
                    1,
                    1,
                    symbol);
        }
    }
    
}

#endif