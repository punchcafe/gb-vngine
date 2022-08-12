#ifndef SELECTION_BOX_DEFINITION
#define SELECTION_BOX_DEFINITION
#include <gb/hardware.h>
/*
New approach for front end bits. These are utility front end functions which can render the
same thing
*/


// TODO: maybe don't have it be a file selector at all, but just a "selection box" where you provide strings
struct SelectionBox {
    char ** options;
    unsigned short number_of_options;
    unsigned short selected_index;
    unsigned short x_start;
    unsigned short x_end;
    unsigned short y_start;
    unsigned short y_end;
};

// TODO: utility methods for populating the file selector context
// TODO: can use the recent functions for writing text
// TODO: use these in the pause screen
// TODO: empty in the missing


void selection_box_populate(struct SelectionBox * box, char ** options, unsigned short number_of_options)
{
    box->options = options;
    box->number_of_options = number_of_options;
    box->selected_index = 0;
}

void selection_box_render(struct SelectionBox * box,
                            unsigned short x_start,
                            unsigned short x_end,
                            unsigned short y_start,
                            unsigned short y_end)
{
        box->x_start = x_start;
        box->x_end = x_end;
        box->y_start = y_start;
        box->y_end = y_end;
        unsigned char blank_tile_array [] = {BLANK_SPACE_CHAR_POSITION};
        for(int x = x_start; x < x_end; x++)
        {
            for(int y = y_start; y < y_end; y++)
            {
                // TODO: if need be optimise this to not check each time.
                if(is_win_enabled())
                {
                    set_win_tiles(x, y, 1, 1, blank_tile_array);
                } else {
                    set_bkg_tiles(x, y, 1, 1, blank_tile_array);
                }
            }
        }
        // | |->| |text|
        unsigned short text_start_x = box-> x_start + 3;
        for(unsigned short i = 0; i < box->number_of_options; i++)
        {
            // Give one row of padding? Could also parameterize this.
            unsigned short text_start_y = y_start + (i*2);
            char * text = box->options[i];
            unsigned short x_cursor = text_start_x;
            while(*text != '\0')
            {
                print_char_at(*text, x_cursor, text_start_y);
                text++;
                x_cursor++;
            }
        }
}

void selection_box_cursor_up(struct SelectionBox * box)
{
}
void selection_box_cursor_down(struct SelectionBox * box)
{
}

// 0 starting
unsigned short selection_box_selected_index(struct SelectionBox * box)
{
    return 1;
}

#endif
