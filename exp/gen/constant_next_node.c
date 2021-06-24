#ifndef CONSTANT_NEXT_NODE
#define CONSTANT_NEXT_NODE

#include "../types/narrative.c"
#include "../types/node.c"
#include "./game_state_mutations.c"

struct Narrative constant_node_narrative = {"how are you, friend?!"};
struct Node n_id_node_2 = {CONSTANT_OPT_FOR_DEV, 0x00, &constant_node_narrative, &do_nothing};

#endif