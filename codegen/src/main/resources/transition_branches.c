#ifndef TRANSITION_BRANCH_DEFINITION
#define TRANSITION_BRANCH_DEFINITION

#define NULL_PREDICATE_POINTER 0x00
#define MAX_NUMBER_OF_BRANCHES 4

// TODO: future optimisation: add banking to branches

struct PlayerBasedTransition
{
    char ** prompts;
    struct Branch ** branches;
    short number_of_branches;
};

struct Branch {
    int (*predicate)(struct GameState *);
    struct Node* node;
};

struct PredicateBasedTransition {
    // TODO: consider rendering branches next to one another to only need single pointer
    struct Branch ** branches;
    int number_of_branches;
};
#endif