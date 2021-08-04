#ifndef FORGROUND_ELEMENT_DEFINITION
#define FORGROUND_ELEMENT_DEFINITION

enum ForegroundElementPosition {LEFT, RIGHT, CENTER};

struct ForegroundElement
{
    enum ForegroundElementPosition position;
    unsigned char *pattern;
};


#endif
