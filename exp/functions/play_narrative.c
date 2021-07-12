#ifndef PLAY_NARRATIVE
#define PLAY_NARRATIVE
#include <stdio.h>
#include "../types/node.c"
#include "../types/narrative.c"

void play_narrative(struct Node * node)
{
    printf("\n");
    printf("\n");
    printf(node->narrative->simple_speech_text);
}
#endif