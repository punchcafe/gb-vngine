#ifndef PLAY_NARRATIVE
#define PLAY_NARRATIVE
#include <stdio.h>
#include "../types/node.c"
#include "../types/narrative.c"

#define ALPHABET_TILESET_OFFSET SOMENUM;
#define BLANK_TEXT_CHARACTER_TILE_OFFSET 0
#define TEXT_BOX_TILE_WIDTH 16
#define TEXT_BOX_TILE_HEIGHT 5
#define TEXT_BOX_TILE_AREA TEXT_BOX_TILE_WIDTH * TEXT_BOX_TILE_HEIGHT

void play_narrative(struct Node * node)
{
    printf("\n");
    printf("\n");
    printf(node->narrative->simple_speech_text);
}


// TODO: extract to text box file

unsigned short text_cursor;


void text_box_write_char(char char_to_write){
    if(text_cursor == TEXT_BOX_TILE_AREA - 1){
        text_cursor = 0;
        text_box_clear();
    } else {
        text_cursor++;
    }
    // extract to a write_to_cursor_position() method 
    short x = text_cursor % TEXT_BOX_TILE_WIDTH;
    short y = text_cursor / TEXT_BOX_TILE_HEIGHT;

}

unsigned char get_letter_position(char utcCharacter){
    switch (utcCharacter)
    {
    case 'a':
        /* code */
        break;
    // ETC
    default:
        break;
    }
}
#endif