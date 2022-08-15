#ifndef GET_NEXT_NODE_FUNCTION_DEFINITION
#define GET_NEXT_NODE_FUNCTION_DEFINITION

#define UP_BUTTON_CHOICE_INDEX 0
#define RIGHT_BUTTON_CHOICE_INDEX 1
#define DOWN_BUTTON_CHOICE_INDEX 2
#define LEFT_BUTTON_CHOICE_INDEX 3

const void (*special_char_renderers[4])()  = {&text_box_print_special_char_up,
&text_box_print_special_char_right,
&text_box_print_special_char_down,
&text_box_print_special_char_left};

void print_choices(char ** prompts, short * available_choices, short number_of_available_choices){
    clear_text_box();
    // TODO: make this async
    for(int i = 0; i < number_of_available_choices; i++)
    {
        text_box_print("\n", 1);
        special_char_renderers[i]();
        text_box_print(prompts[available_choices[i]], 1);
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
        delay_with_music(1);
    }
}

struct Node * get_player_based_node(struct PlayerBasedTransition * transition,
                                    struct GameState * game_state)
{
    unsigned short available_choices [MAX_NUMBER_OF_BRANCHES];
    unsigned short number_of_available_choices = 0;
    for(int i = 0; i < transition->number_of_branches; i++)
    {
        if(transition->branches[i]->predicate == NULL_PREDICATE_POINTER
            || transition->branches[i]->predicate(game_state)){
            available_choices[number_of_available_choices] = i;
            number_of_available_choices++;
        }
    }

    print_choices(transition->prompts, available_choices, number_of_available_choices);
    short options_choice = get_choice_index(number_of_available_choices);
    short branch_choice = available_choices[options_choice];
    return transition->branches[branch_choice]->node;
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

// TODO: module namespacing.

struct GetNextNodeResult {
    unsigned char finished;
    struct Node * next_node;
};

void _set_node_result(struct Node * node, struct GetNextNodeResult * result)
{
    result->next_node = node;
    result->finished = 0x01;
}

void _set_node_result_unfinished(struct GetNextNodeResult * result)
{
    result->next_node = 0x00;
    result->finished = 0x00;
}


struct Node * get_next_node(struct Node * node,
                            struct GameState * game_state
                            /*struct GetNextNodeResult * out*/){
    if(node->node_transition_type == PLAYER_BASED_TRANSITION){
        return get_player_based_node((struct PlayerBasedTransition *)node->node_transition_object, game_state);
    } else if(node->node_transition_type == PREDICATE_BASED_TRANSITION){
        return get_predicate_based_node((struct PredicateBasedTransition *)(node->node_transition_object), game_state);
    };
    return GAME_OVER_NODE_ID;
}


void maybe_set_next_node(struct Node ** current_node_ptr,
                                struct GameState * gs,
                                void (*observers)(void),
                                unsigned short num_observers)
{
//
}
#endif