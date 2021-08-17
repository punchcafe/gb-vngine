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
    unsigned char updated_sign_off [BACKGROUND_TILE_AREA];
    for(int i = 0; i < BACKGROUND_TILE_AREA; i++)
    {
        unsigned char shifted_index = element->tile_assignements[i] + TEXT_PATTERNS_END_INDEX + 1;
        updated_sign_off[i] = shifted_index;
    }
    set_bkg_data(TEXT_PATTERNS_END_INDEX + 1, element->number_of_tiles, element->background_tiles);
    set_bkg_tiles(0x00, 0x00, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, updated_sign_off);
}

#endif