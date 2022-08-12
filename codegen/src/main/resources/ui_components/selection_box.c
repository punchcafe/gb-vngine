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

unsigned char _selection_box_blank_tile_array [] = {BLANK_SPACE_CHAR_POSITION};
unsigned char _selection_box_cursor_tile_array [] = {PRESS_RIGHT_BUTTON_PRESSED_POSITION};

//TODO: make utility function

void util_set_tiles(unsigned short x,
                    unsigned short y,
                    unsigned short w,
                    unsigned short h,
                    unsigned char * data)
{
    if(is_win_enabled()){
        set_win_tiles(x, y, w, h, data);
    } else {
        set_bkg_tiles(x, y, w, h, data);
    }
}

unsigned short _selection_box_cursor_x(struct SelectionBox * box)
{
  return box->x_start;
}


unsigned short _selection_box_cursor_y(struct SelectionBox * box)
{
  return (box->selected_index) * 2 + box->y_start;
}

void _selection_box_draw_cursor(struct SelectionBox * box)
{
        util_set_tiles(_selection_box_cursor_x(box),
                  _selection_box_cursor_y(box),
                  1,
                  1,
                  _selection_box_cursor_tile_array);
}

void _selection_box_clear_cursor(struct SelectionBox * box)
{
        util_set_tiles(_selection_box_cursor_x(box),
                  _selection_box_cursor_y(box),
                  1,
                  1,
                  _selection_box_blank_tile_array);
}

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
        // TODO: potentially make this static (but consider implication
        for(int x = x_start; x < x_end; x++)
        {
            for(int y = y_start; y < y_end; y++)
            {
                 util_set_tiles(x, y, 1, 1, _selection_box_blank_tile_array);
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
        _selection_box_draw_cursor(box);
}

void _selection_box_clear_and_set_cursor(struct SelectionBox * box, unsigned short new_index)
{
    _selection_box_clear_cursor(box);
    box->selected_index = new_index;
    _selection_box_draw_cursor(box);
}

void selection_box_cursor_up(struct SelectionBox * box)
{
    unsigned short new_index = (box->selected_index - 1) % box->number_of_options;
    _selection_box_clear_and_set_cursor(box, new_index);
}
void selection_box_cursor_down(struct SelectionBox * box)
{
    unsigned short new_index = (box->selected_index + 1) % box->number_of_options;
    _selection_box_clear_and_set_cursor(box, new_index);
}




// 0 starting
unsigned short selection_box_selected_index(struct SelectionBox * box)
{
    return 1;
}

#endif
