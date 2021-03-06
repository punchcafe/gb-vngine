#ifndef TEXT_RENDER_DEFINITION

#define ALPHABET_START_OFFSET 6

unsigned char black_tiles[] = {
    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00,
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 0x00, 0x00
};


char get_char_position(unsigned char character)
{
    if(character == ' ')
    {
    return BLANK_SPACE_CHAR_POSITION;
    }
    return get_char_position_without_offset(character) + ALPHABET_START_OFFSET;
}

#define TEXT_WIDTH 18
#define NUMBER_OF_ROWS 6
#define TEXT_COLUMN_OFFSET 1
#define CURSOR_MAX_EXCLUSIVE TEXT_WIDTH * NUMBER_OF_ROWS
#define TEXT_DELAY 50
#define TEXT_PATTERNS_END_INDEX 80

int cursor = 0;

/*
returns 0 if the full string is rendered, otherwise returns an int of the offset of the string.
*/

void write_position_and_increment_cursor(unsigned char tile_number, unsigned int bank_num)
{
    if(cursor < CURSOR_MAX_EXCLUSIVE)
    {
        short x = (cursor % TEXT_WIDTH) + TEXT_COLUMN_OFFSET;
        short y = (cursor / TEXT_WIDTH) + 12;
        unsigned char tile_number_array [1]= {tile_number};
        set_bkg_tiles(x, y, 1, 1, tile_number_array);
        delay_with_music(2);
        // god save us we need to refactor this abomination
        SWITCH_ROM_MBC1(bank_num);
        cursor++;
    }
}

void new_line()
{
    if(cursor == (CURSOR_MAX_EXCLUSIVE - 1))
    {
        await_a_button_press();
        clear_text_box();
        return;
    }
    int row = cursor / TEXT_WIDTH;
    if(row >= (NUMBER_OF_ROWS - 1))
    {
        cursor = CURSOR_MAX_EXCLUSIVE - 1;
        return;
    }
    cursor = (row + 1) * TEXT_WIDTH;
}

int text_box_print(char * str_char, unsigned int bank_num)
{

   // TODO: refactor and make less terrible
    SWITCH_ROM_MBC1(bank_num);
    int offset = 0;
    while(*str_char != '\0' && cursor < CURSOR_MAX_EXCLUSIVE)
    {
        SWITCH_ROM_MBC1(bank_num);
        if(*str_char == ' ')
        {
            if(str_char[1] == ' ')
            {
                // ignore until find single space
                str_char++;
                offset++;
                continue;
            }
            unsigned int word_length = 0;
            unsigned int character_space_left_on_line = TEXT_WIDTH - ((cursor + 1) % TEXT_WIDTH);
            unsigned int look_ahead_letter = 1;
            while(str_char[look_ahead_letter] != ' ')
            {
                word_length++;
                look_ahead_letter++;
            }
            if(word_length > character_space_left_on_line)
            {
                new_line();
                offset++;
                str_char++;
                continue;
            } else {
            // DUPLICATED
                unsigned char tile_number = get_char_position(*str_char);
                write_position_and_increment_cursor(tile_number, bank_num);
                offset++;
                str_char++;
                continue;
            }
        }
        if(*str_char == '\n')
        {
            new_line();
            offset++;
            str_char++;
            continue;
        }
        unsigned char tile_number = get_char_position(*str_char);
        write_position_and_increment_cursor(tile_number, bank_num);
        offset++;
        str_char++;
    }
    if(*str_char == '\0')
    {
        return 0;
    } else {
        return  offset;
    }
}

void text_box_print_special_char_up() {
    write_position_and_increment_cursor(PRESS_UP_BUTTON_PRESSED_POSITION, 1);
}

void text_box_print_special_char_down() {
    write_position_and_increment_cursor(PRESS_DOWN_BUTTON_PRESSED_POSITION, 1);
}

void text_box_print_special_char_left() {
    write_position_and_increment_cursor(PRESS_LEFT_BUTTON_PRESSED_POSITION, 1);
}

void text_box_print_special_char_right() {
    write_position_and_increment_cursor(PRESS_RIGHT_BUTTON_PRESSED_POSITION, 1);
}

void clear_text_box(){
    cursor = 0;
    set_bkg_tiles(0, 12, 20, 6, black_tiles);
}

void render_whole_text(char * text, unsigned int bank_num)
{
    clear_text_box();
    int rendered_offset = text_box_print(text, bank_num);
    while(rendered_offset > 0){
        await_a_button_press();
        clear_text_box();
        text += rendered_offset;
        rendered_offset = text_box_print(text, bank_num);
    }
}

int a_button_is_pressed()
{
    return joypad() & J_A;
}



void await_a_button_press()
{
    unsigned int blinking_a_button = 2;
    int loops = 0;
    while(!a_button_is_pressed()){
        if(loops % 6 == 0){
            // every 300 ms
            int blinking_a_ref [] = {blinking_a_button - 1};
            set_bkg_tiles(19,17, 1,1,blinking_a_ref);
            blinking_a_button = ((blinking_a_button + 1) % 2) + 2;
        }
        delay_with_music(4);
        loops++;
    }
}

void initialise_font(char * font_set_data, unsigned int font_set_data_size){
    set_bkg_data(0x00, BUTTON_SET_SIZE, button_tile_set_data);
    set_bkg_data(BUTTON_SET_SIZE, font_set_data_size, font_set_data);
    set_bkg_tiles(0, 12, 20, 6, black_tiles);
}

#endif