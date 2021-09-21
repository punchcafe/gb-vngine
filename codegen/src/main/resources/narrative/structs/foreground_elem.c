#ifndef FORGROUND_ELEMENT_DEFINITION
#define FORGROUND_ELEMENT_DEFINITION

enum ForegroundElementPosition {LEFT_PORTRAIT, RIGHT_PORTRAIT, CENTER_PORTRAIT, CENTER_FOCUS};

//TODO: implement use_pattern_block(number)
// checks if global pattern number is set already

struct ForegroundElement
{
    enum ForegroundElementPosition position;
    unsigned short pattern_block_number;
    // TODO: implement
    // unsigned short bank_number;
    unsigned char * pattern_references;
};
#endif
