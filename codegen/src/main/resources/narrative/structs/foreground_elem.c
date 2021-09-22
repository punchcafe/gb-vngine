#ifndef FORGROUND_ELEMENT_DEFINITION
#define FORGROUND_ELEMENT_DEFINITION

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

struct ForegroundElement {
    PortraitAsset * asset;
    ForegroundElementPosition position;
}

struct ForegroundAsset
{
    unsigned short pattern_block_number;
    // unsigned short bank_number;
    unsigned char * pattern_references;
};
#endif
