#include "./lcc-compliant.c"
/*
    instead of having a method per branch predicate, we can treat the predicates as a set,
    where an expression defines uniqueness (after being transformed by stuff like lowecasing, 
    replacing white space with underscore etc). So any duplicate expressions are only generated
    once and used by each

*/

int GAMESTATE_PREDICATE_predicate_expression(struct GAMESTATE_GameState * game_state){
    return 0; // False
}

int GAMESTATE_PREDICATE_predicate_expression_2(struct GAMESTATE_GameState * game_state){
    return 1; // true
}

typedef struct Branch {
    int (*predicate)(struct GAMESTATE_GameState * game_state);
    struct NODE_Node next_node;
} Branch;

typedef struct PredicateDefinedNextBranch {
    Branch * branches_array;
    short number_of_branches;
    // array of branch pointers
    // number of brainches in array
} PredicateDefinedNextBranch;