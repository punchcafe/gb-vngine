// Temporary implementation for pause mode until moving to an async process model
#ifndef PAUSE_MODE_H
#define PAUSE_MODE_H

#include <stdio.h>
#include <gb/gb.h>

#define TILES_HEIGHT 18
#define TILES_WIDTH 20

//enum PauseMode { SAVE, LOAD };
//enum PauseMode current_pause_mode = LOAD;


void pause_mode()
{
    for(int i = 0; i < TILES_WIDTH; i++)
    {
      for(int j = 0; j < TILES_HEIGHT; j++)
      {
        set_win_tiles(i, j, 1, 1, 1);
      }
    }
    HIDE_SPRITES;
    SHOW_WIN;

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