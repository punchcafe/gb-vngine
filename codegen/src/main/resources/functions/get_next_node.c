#ifndef GET_NEXT_NODE_FUNCTION_DEFINITION
#define GET_NEXT_NODE_FUNCTION_DEFINITION

#define UP_BUTTON_CHOICE_INDEX 0
#define RIGHT_BUTTON_CHOICE_INDEX 1
#define DOWN_BUTTON_CHOICE_INDEX 2
#define LEFT_BUTTON_CHOICE_INDEX 3

const char* prefix_array []  = {"up ", "rt ", "dn ", "lt "};

void print_choices(char ** prompts, short number_of_prompts){
    clear_text_box();
    for(int i = 0; i < number_of_prompts; i++)
    {
        text_box_print(prefix_array[i]);
        text_box_print(prompts[i]);
    }
}

short get_choice_index(int number_of_prompts){
    short choice = 5;
    while(1)
    {
        unsigned char input = joypad();
        if(input & J_UP) {
            choice = UP_BUTTON_CHOICE_INDEX;
        } else if(input & J_RIGHT) {
            choice = RIGHT_BUTTON_CHOICE_INDEX;
        } else if(input & J_DOWN) {
            choice = DOWN_BUTTON_CHOICE_INDEX;
        } else if(input & J_LEFT) {
            choice = LEFT_BUTTON_CHOICE_INDEX;
        };
        if(choice < number_of_prompts){
            return choice;
        }
    }
}

struct Node * get_player_based_node(struct PlayerBasedTransition * transition,
                                    struct GameState * game_state)
{
    print_choices(transition->prompts, transition->number_of_prompts);
    short choice = get_choice_index(transition->number_of_prompts);
    return transition->node_choices[choice];
}

struct Node * get_predicate_based_node(struct PredicateBasedTransition * transition,
                                       struct GameState * game_state)
{
    int number_of_branches = transition->number_of_branches;
    for(int i = 0; i < number_of_branches; i++){
        if(transition->branches[i]->predicate(game_state)){
            return transition->branches[i]->node;
        }
    }
    //TODO: implement default when in schema
    return GAME_OVER_NODE_ID;
}

struct Node * get_next_node(struct Node * node, struct GameState * game_state){
    if(node->node_transition_type == PLAYER_BASED_TRANSITION){
        return get_player_based_node((struct PlayerBasedTransition *)node->node_transition_object, game_state);
    } else if(node->node_transition_type == PREDICATE_BASED_TRANSITION){
        return get_predicate_based_node((struct PredicateBasedTransition *)(node->node_transition_object), game_state);
    };
    return GAME_OVER_NODE_ID;
}
#endif