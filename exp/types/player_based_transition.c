#ifndef PLAYER_BASED_TRANSITION_DEFINITION
#define PLAYER_BASED_TRANSITION_DEFINITION
#include "../types/node.c"
struct PlayerBasedTransition
{
    char ** prompts;
    struct Node ** node_choices;
    short number_of_prompts;
};

#endif