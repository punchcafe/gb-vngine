#include "./game_state.c"
#include "./narrative.c"
#ifndef NODE_TYPE_DEFINITION
#define NODE_TYPE_DEFINITION

enum NodeTransitionType {
    PLAYER_BASED_TRANSITION, 
    PREDICATE_BASED_TRANSITION,
    CONSTANT_OPT_FOR_DEV
};

struct Node {
    enum NodeTransitionType node_transition_type;
    void * node_transition_object;
    struct Narrative * narrative;
    void (*game_state_modification)(struct GameState * game_state);
};
#endif