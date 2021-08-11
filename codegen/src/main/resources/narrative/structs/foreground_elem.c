#ifndef FORGROUND_ELEMENT_DEFINITION
#define FORGROUND_ELEMENT_DEFINITION

enum ForegroundElementPosition {LEFT_PORTRAIT, RIGHT_PORTRAIT, CENTER_PORTRAIT, CENTER_FOCUS};

struct ForegroundElement
{
    enum ForegroundElementPosition position;
    unsigned char *pattern;
};
#endif
