// Temporary implementation for pause mode until moving to an async process model
#ifndef PAUSE_MODE_H
#define PAUSE_MODE_H

#include <stdio.h>
#include <gb/gb.h>

#define TILES_HEIGHT 18
#define TILES_WIDTH 20

//enum PauseMode { SAVE, LOAD };
//enum PauseMode current_pause_mode = LOAD;

char * opts [] = {"file k", "file b", "file c"};
struct SelectionBox sample_selection = {opts,2,0,0,0,0,0};
void pause_mode()
{
    char  tiles []= {0x00};
    for(int i = 0; i < TILES_WIDTH; i++)
    {
      for(int j = 0; j < TILES_HEIGHT; j++)
      {
        set_win_tiles(i, j, 1, 1, tiles);
      }
    }
    HIDE_SPRITES;
    SHOW_WIN;

    print_char_at('p', 10, 3);
    print_char_at('a', 11, 3);
    print_char_at('u', 12, 3);
    print_char_at('s', 13, 3);
    print_char_at('e', 14, 3);

    selection_box_render(&sample_selection, 1, 20, 7, 15);

    delay(500);
    unsigned char input = joypad();
    while(!(input & J_START))
    {
        input=joypad();
    }
    SHOW_SPRITES;
    HIDE_WIN;
    delay(500);
}

#endif