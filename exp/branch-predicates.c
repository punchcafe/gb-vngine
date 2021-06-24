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
    struct NODE_Node * next_node;
} Branch;

typedef struct PredicateDefinedNextBranches {
    Branch * branches_array;
    short number_of_branches;
    // array of branch pointers
    // number of brainches in array
} PredicateDefinedNextBranch;

struct NODE_Node * get_next_node(PredicateDefinedNextBranches * branches, struct GAMESTATE_GameState * game_state)
{
    int limit = branches->number_of_branches;
    for(int i = 0;i < limit; i++)
    {
        Branch * branch = branches->branches_array[i];
        if(branch->predicate(game_state))
        {
            return branch->node;
        }
    }
    // Return default
}
/*
make dependency graph of what types (predicates/GSM, type definitions (separate file), statics etc) depend on what types in 
dependency graph
*/