
#ifndef FOREGROUND_RENDER_DEFINITION
#define FOREGROUND_RENDER_DEFINITION

#define FOCUS_MODE_X_OFFSET 45
#define FOCUS_MODE_Y_OFFSET 40

#define LEFT_PORTRAIT_MODE_X_OFFSET 16
#define RIGHT_PORTRAIT_MODE_X_OFFSET 88
#define PORTRAIT_MODE_Y_OFFSET 16

void set_focus_tile(unsigned char * patterns)
{
  HIDE_SPRITES;
  set_sprite_data(0, (2 * 2)*80, patterns);
  for(int i = 0; i < 40; i++)
  {
    set_sprite_tile(i, i*2);
    move_sprite(i,(i%10)*8 + FOCUS_MODE_X_OFFSET, (i/10)*16 + FOCUS_MODE_Y_OFFSET);
  }
  delay(50);
  SHOW_SPRITES;
}

void set_character_tile(unsigned short left_offset, unsigned char * patterns)
{
    HIDE_SPRITES;
    set_sprite_data(0, 80, patterns);
    for(unsigned short i_0 = 0; i_0 < 5; i_0++)
    {
      // render first row missing top left and right dual tiles
      unsigned short x_0 = (i_0*8) + 8 + 8 + left_offset;
      unsigned short y_0 = PORTRAIT_MODE_Y_OFFSET;
      set_sprite_tile(i_0, i_0*2);
      move_sprite(i_0, x_0, y_0);

    }
    for(unsigned short i = 7; i < 43; i++)
    {
        // accomodate for missing two tiles in first row, also adjusts sprite num
        unsigned int sprite_number = i - 2;
        unsigned short x = (i%7) * 8 + 8 + left_offset;
        unsigned short y = (i/7) * 16 + PORTRAIT_MODE_Y_OFFSET;
        set_sprite_tile(sprite_number, sprite_number * 2);
        move_sprite(sprite_number, x, y);
    }
    delay(50);
    SHOW_SPRITES;
}

void set_character_tile_left(unsigned char * patterns)
{
  set_character_tile(LEFT_PORTRAIT_MODE_X_OFFSET, patterns);
}

void set_character_tile_right(unsigned char * patterns)
{
  set_character_tile(RIGHT_PORTRAIT_MODE_X_OFFSET, patterns);
}

#endif