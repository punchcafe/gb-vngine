#ifndef FORGROUND_ELEMENT_DEFINITION
#define FORGROUND_ELEMENT_DEFINITION


#define FOCUS_MODE_X_OFFSET 45
#define FOCUS_MODE_Y_OFFSET 40

#define LEFT_PORTRAIT_MODE_X_OFFSET 16
#define CENTER_PORTRAIT_MODE_X_OFFSET 52
#define RIGHT_PORTRAIT_MODE_X_OFFSET 88
#define PORTRAIT_MODE_Y_OFFSET 16

enum ForegroundElementPosition {LEFT_PORTRAIT, RIGHT_PORTRAIT, CENTER_PORTRAIT, CENTER_FOCUS};

//TODO: implement use_pattern_block_in_vram(number)
// checks if global pattern number is set already

/*
unsigned short current_block_num_in_vram = 0;

const unsigned char pattern_block_0 [] {..};
const unsigned char pattern_block_1 [] {..};
const unsigned char pattern_block_2 [] {..};
const unsigned char pattern_block_3 [] {..};

const unsigned short all_data_block_sizes [] {100, 95...};
const unsigned char * all_data_blocks [] {pattern_block_0, pattern_block_1...};

void use_pattern_block(unsigned short block_num)
{
    if(current_block_num_var == block_num) return;
    set_sprite_data(SPRITE_STARTING_INDEX, all_data_blocks[block_num], all_data_block_size[block_num]);
}

enum Position current_foreground_position;


move_sprites(enum Position){
    // if in correct position, do not bother.
}
...
play_foreground(asset) {
    use_pattern_block(asset->pattern_block)
    set_sprite_data(asset.data)
    move_sprites()
}


// should make subsequent reaction shots very quick and cheap
*/

// TODO: acknowledge coupling of position to type of element

struct ForegroundAsset
{
    unsigned short pattern_block_number;
    // unsigned short bank_number;
    unsigned char * pattern_references;
};

// TODO: clean up, separate and refactor
/*
    Narrative element to be played by narrative player.
*/
struct ForegroundElement {
    enum ForegroundElementPosition position;
    struct ForegroundAsset * asset;
};

int current_block_num_in_vram = 0;

void set_pattern_block(unsigned short index)
{
  if(current_block_num_in_vram != index)
  {
      set_sprite_data(0,
          foreground_asset_pattern_block_sizes[index],
          foreground_asset_pattern_block_index[index]);
      current_block_num_in_vram = index;
  }
}

void set_focus_tile(struct ForegroundAsset * asset)
{
  HIDE_SPRITES;
  set_pattern_block(asset->pattern_block_number);
  for(int i = 0; i < 40; i++)
  {
    set_sprite_tile(i, asset->pattern_references[i]);
    move_sprite(i,(i%10)*8 + FOCUS_MODE_X_OFFSET, (i/10)*16 + FOCUS_MODE_Y_OFFSET);
  }
  delay_with_music(4);
  SHOW_SPRITES;
}

unsigned short current_foreground_offset = 0;

void set_character_tile(unsigned short left_offset, struct ForegroundAsset * asset)
{
    HIDE_SPRITES;
    set_pattern_block(asset->pattern_block_number);

    if(left_offset == current_foreground_offset){
        return;
    }

    for(unsigned short i_a = 0; i_a < 40; i_a++)
    {
        set_sprite_tile(i_a, asset->pattern_references[i_a]);
    }

    for(unsigned short i_0 = 0; i_0 < 5; i_0++)
    {
      // render first row missing top left and right dual tiles
      unsigned short x_0 = (i_0*8) + 8 + 8 + left_offset;
      unsigned short y_0 = PORTRAIT_MODE_Y_OFFSET;
      move_sprite(i_0, x_0, y_0);

    }
    for(unsigned short i = 7; i < 42; i++)
    {
        // accomodate for missing two tiles in first row, also adjusts sprite num
        unsigned int sprite_number = i - 2;
        unsigned short x = (i%7) * 8 + 8 + left_offset;
        unsigned short y = (i/7) * 16 + PORTRAIT_MODE_Y_OFFSET;
        move_sprite(sprite_number, x, y);
    }
    current_foreground_offset = left_offset;
    delay_with_music(4);
    SHOW_SPRITES;
}

void set_character_tile_left(struct ForegroundAsset * asset)
{
  set_character_tile(LEFT_PORTRAIT_MODE_X_OFFSET, asset);
}

void set_character_tile_right(struct ForegroundAsset * asset)
{
  set_character_tile(RIGHT_PORTRAIT_MODE_X_OFFSET, asset);
}

void set_character_tile_center(struct ForegroundAsset * asset)
{
  set_character_tile(CENTER_PORTRAIT_MODE_X_OFFSET, asset);
}

void handle_foreground(struct ForegroundElement* foreground)
{
    switch (foreground->position)
    {
    case LEFT_PORTRAIT:
        set_character_tile_left(foreground->asset);
        break;
    case RIGHT_PORTRAIT:
        set_character_tile_right(foreground->asset);
        break;
    case CENTER_PORTRAIT:
        set_character_tile_center(foreground->asset);
        break;
    case CENTER_FOCUS:
        set_focus_tile(foreground->asset);
        break;
    default:
        break;
    }
}

#endif
