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
// TODO: separate assets from narrative
// TODO: consider being able to switch banks between patterns and block_references
#ifndef FORGROUND_ASSET_STRUCT_DEFINITION
#define FORGROUND_ASSET_STRUCT_DEFINITION

struct PatternBlock {
    unsigned char * data;
    unsigned int size;
    unsigned int bank_number;
};

struct ForegroundAsset
{
    const struct PatternBlock * block;
    const unsigned char * pattern_block_references;
};
#endif

struct ExternalForegroundAsset
{
    struct ForegroundAsset * asset;
    unsigned int bank_number;
};

// TODO: extract to utility class
unsigned int val_or_clamp(unsigned int value, unsigned int clamp)
{
    if(value > clamp)
    {
        return clamp;
    }
    return value;
}

// TODO: clean up, separate and refactor
/*
    Narrative element to be played by narrative player.
*/
struct ForegroundElement {
    enum ForegroundElementPosition position;
    struct ExternalForegroundAsset * asset;
};

#define SPRITES_PER_VBLANK 5

struct ForegroundNarrativeState {
    // offset tracker, used during tile assignment and moving
    unsigned int tile_offset;
    // After setting sprites, we must wait a short delay before making them visible.
    // This is to give time to the screen to finish processing graphics.
    unsigned int await_screen_acc;
    unsigned char awaiting_screen;
    enum ForegroundElementPosition previous_sprite_position;
    // Optimisation derived from the previous sprite position during initalization
    unsigned char must_move_sprites;
};

unsigned int current_bank = 0;
struct PatternBlock * current_block_in_vram = 0x00;

void set_pattern_block(struct PatternBlock * pattern_block)
{
//todo: factor in case where address is the same but block is different
//  if(current_block_in_vram != pattern_block)
//  {
      set_sprite_data(0,
          pattern_block->size,
          pattern_block->data);
      current_block_in_vram = pattern_block;
//  }
}

unsigned int set_focus_tile(struct ExternalForegroundAsset * external_asset,
                             struct ForegroundNarrativeState * render_state)
{
  SWITCH_ROM_MBC1(external_asset->bank_number);
  struct ForegroundAsset * asset = external_asset -> asset;
  // TODO: move to init

  int limit = val_or_clamp(render_state->tile_offset + SPRITES_PER_VBLANK, 40);
  for(int i = render_state->tile_offset; i < limit; i++)
  {
    set_sprite_tile(i, asset->pattern_block_references[i]);
    move_sprite(i,(i%10)*8 + FOCUS_MODE_X_OFFSET, (i/10)*16 + FOCUS_MODE_Y_OFFSET);
  }
  if(limit == 40)
  {
    SHOW_SPRITES;
    return 1;
  } else {
    render_state->tile_offset = limit;
    return 0;
  }
}

unsigned short current_foreground_offset = 0;

unsigned char moving_sprites_mode = 0x01;
unsigned int set_character_tile(unsigned short left_offset,
                                struct ExternalForegroundAsset * external_asset,
                                struct ForegroundNarrativeState * render_state)
{
    // TODO: extract to method above so handled homogeounsly for all visual types
    if(render_state->awaiting_screen)
    {
        // This state is reached at the end, when everything has been moved and set,
        // we are just waiting for the screen to finish rendering
        render_state->await_screen_acc++;
        if(render_state->await_screen_acc > 20)
        {
            SHOW_SPRITES;
            return 0x01;
        };
        return 0x00;
    }
    SWITCH_ROM_MBC1(external_asset->bank_number);
    struct ForegroundAsset * asset = external_asset->asset;
    //set_pattern_block(asset->block);
    int limit = val_or_clamp(render_state->tile_offset + SPRITES_PER_VBLANK, 40);

    if(render_state->must_move_sprites){
        // ensure we don't move sprites unless needed

        if(render_state->tile_offset < 5)
        {
            for(unsigned short i_0 = render_state->tile_offset; i_0 < 5; i_0++)
            {
            set_sprite_tile(i_0, asset->pattern_block_references[i_0]);
            // render first row missing top left and right dual tiles
            unsigned short x_0 = (i_0*8) + 8 + 8 + left_offset;
            unsigned short y_0 = PORTRAIT_MODE_Y_OFFSET;
            move_sprite(i_0, x_0, y_0);
            }
            render_state->tile_offset = 5;
        }


        // Offsetted by two for the sprite array pyramid shape
        for(unsigned short i = render_state->tile_offset + 2; i < limit + 2; i++)
        {
            // accomodate for missing two tiles in first row, also adjusts sprite num
            unsigned int sprite_number = i - 2;
            set_sprite_tile(sprite_number, asset->pattern_block_references[sprite_number]);
            unsigned short x = (i%7) * 8 + 8 + left_offset;
            unsigned short y = (i/7) * 16 + PORTRAIT_MODE_Y_OFFSET;
            move_sprite(sprite_number, x, y);
        }
        // TODO: extract to state

    } else {
        // Only need to change tiles, not move them
        for(unsigned short i_a = 0; i_a < 40; i_a++)
        {
            set_sprite_tile(i_a, asset->pattern_block_references[i_a]);
        }
    }
    if(limit >= 40)
    {
        render_state->awaiting_screen = 0x01;
    } else {
        render_state->tile_offset = limit;
    }
    return 0x00;
}

unsigned int set_character_tile_left(struct ExternalForegroundAsset * asset,
                                    struct ForegroundNarrativeState * render_state)
{
  return set_character_tile(LEFT_PORTRAIT_MODE_X_OFFSET, asset, render_state);
}

unsigned int set_character_tile_right(struct ExternalForegroundAsset * asset,
                                    struct ForegroundNarrativeState * render_state)
{
  return set_character_tile(RIGHT_PORTRAIT_MODE_X_OFFSET, asset, render_state);
}

unsigned int set_character_tile_center(struct ExternalForegroundAsset * asset,
                                    struct ForegroundNarrativeState * render_state)
{
  return set_character_tile(CENTER_PORTRAIT_MODE_X_OFFSET, asset, render_state);
}

unsigned int handle_foreground(struct ForegroundElement* foreground, 
                               struct ForegroundNarrativeState * render_state)
{
    HIDE_SPRITES;
    switch (foreground->position)
    {
    case LEFT_PORTRAIT:
        return set_character_tile_left(foreground->asset, render_state);
    case RIGHT_PORTRAIT:
        return set_character_tile_right(foreground->asset, render_state);
    case CENTER_PORTRAIT:
        return set_character_tile_center(foreground->asset, render_state);
    case CENTER_FOCUS:
        return set_focus_tile(foreground->asset, render_state);
    default:
        return 0x01;
    }
}