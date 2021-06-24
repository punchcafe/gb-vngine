#ifndef GET_NEXT_NODE_FUNCTION_DEFINITION
#define GET_NEXT_NODE_FUNCTION_DEFINITION
#include "../defs/null_node.c"
#include "../types/node.c"
#include "../types/player_based_transition.c"
#include "../types/predicate_based_transition.c"
#include "../types/game_state.c"
#include "../gen/constant_next_node.c"
struct Node * get_player_based_node(struct PlayerBasedTransition * tranition, 
                                    struct GameState * game_state)
{
    return &n_id_node_2;
}

struct Node * get_predicate_based_node(struct PredicateBasedTransition * transition, 
                                       struct GameState * game_state)
{
    int number_of_branches = transition->number_of_branches;
    for(int i = 0; i < number_of_branches; i++){
        if(transition->branches[i].predicate(game_state)){
            return transition->branches[i].node;
        }
    }
    //TODO: implement default when in schema
    return NULL_NODE_POINTER;
}

struct Node * get_next_node(struct Node * node, struct GameState * game_state){
    if(node->node_transition_type == PLAYER_BASED_TRANSITION){
        return get_player_based_node((struct PlayerBasedTransition *)node->node_transition_object, game_state);
    } else if(node->node_transition_type == PREDICATE_BASED_TRANSITION){
        return get_predicate_based_node((struct PredicateBasedTransition *)(node->node_transition_object), game_state);
    };
    return NULL_NODE_POINTER;
}
#endif