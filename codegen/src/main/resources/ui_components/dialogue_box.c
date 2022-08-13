#ifndef DIALOGUE_BOX_DEF
#define DIALOGUE_BOX_DEF

#define DIALOGUE_BOX_WIDTH 18
#define DIALOGUE_BOX_HEIGHT 6
#define DIALOGUE_BOX_CHAR_SPACE DIALOGUE_BOX_WIDTH * DIALOGUE_BOX_HEIGHT
#define A_BUTTON_PRESSED_TILE_NUMBER 2
#define A_BUTTON_NOT_PRESSED_TILE_NUMBER 1
//TODO: make global constant, or a scratch buffer for anything which needs it for stuff like this
unsigned char _dialogue_box_black_tiles[] = {
    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00
};

struct DialogueBox {
    unsigned int cursor;
    unsigned char a_button_icon_state;
};

short dialogue_box_cursor_x(struct DialogueBox * box)
{
    return box->cursor % DIALOGUE_BOX_WIDTH;
}

short dialogue_box_cursor_y(struct DialogueBox * box)
{
    return (box->cursor / DIALOGUE_BOX_WIDTH) + 12;
}

// Boolean: return false if at end of text box
int dialogue_box_cursor_increment(struct DialogueBox * box)
{
    if(box->cursor < (DIALOGUE_BOX_CHAR_SPACE - 1))
    {
        box->cursor++;
        return 0x01;
    }
    return 0x00;
}

int dialogue_box_new_line(struct DialogueBox * box)
{
    int new_cursor = box->cursor + DIALOGUE_BOX_WIDTH;
    if(new_cursor < DIALOGUE_BOX_CHAR_SPACE)
    {
        box->cursor = new_cursor;
        return 0x01;
    } else {
        box->cursor = DIALOGUE_BOX_CHAR_SPACE;
        return 0x00;
    }
}

void dialogue_box_clear_screen(struct DialogueBox * box)
{
    box->cursor = 0;
    set_bkg_tiles(0, 12, 20, 6, _dialogue_box_black_tiles);
}

void dialogue_box_print_char(struct DialogueBox * box, unsigned char character)
{
    print_char_at(character,
                    dialogue_box_cursor_x(box),
                    dialogue_box_cursor_y(box));
}
// TODO: dialogue_box_set_cursor(x, y)

void dialogue_box_print_tile(struct DialogueBox * box, unsigned char tile)
{
    print_tile_at(tile,
                    dialogue_box_cursor_x(box),
                    dialogue_box_cursor_y(box));
}

void dialogue_box_toggle_a_button(struct DialogueBox * box)
{
    if(box->a_button_icon_state){
        box->a_button_icon_state = 0x00;
        unsigned char blinking_a_ref [] = {A_BUTTON_PRESSED_TILE_NUMBER};
        set_bkg_tiles(19,17, 1,1,blinking_a_ref);
    } else {
      box->a_button_icon_state = 0x01;
      unsigned char blinking_a_ref [] = {A_BUTTON_NOT_PRESSED_TILE_NUMBER};
      set_bkg_tiles(19,17, 1,1,blinking_a_ref);
    }
}
#endif