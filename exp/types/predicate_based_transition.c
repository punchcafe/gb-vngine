#ifndef PREDICATE_BASED_TRANSITION_DEFINITION
#define PREDICATE_BASED_TRANSITION_DEFINITION
#include "../types/node.c"
struct Branch {
    int (*predicate)(struct GameState *);
    struct Node* node;
};

struct PredicateBasedTransition {
    struct Branch ** branches;
    int number_of_branches;
};
#endif