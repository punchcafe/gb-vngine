#ifndef CONSTANT_NEXT_NODE
#define CONSTANT_NEXT_NODE

#include <stdio.h>
#include "../types/narrative.c"
#include "../types/node.c"
#include "./game_state_mutations.c"
#include "../types/player_based_transition.c"
#include "../types/predicate_based_transition.c"

void(*do_nothing_array[1])(struct GameState *) = {&do_nothing};

struct PredicateBasedTransition node_1_transition;
struct Narrative constant_node_narrative_1 = {"how are you friend?"};
struct Node n_id_node_1 = {PREDICATE_BASED_TRANSITION, &node_1_transition, &constant_node_narrative_1, do_nothing_array};

struct PredicateBasedTransition node_2_transition;
struct Narrative constant_node_narrative_2 = {"I'm fine, and you?"};
struct Node n_id_node_2 = {PREDICATE_BASED_TRANSITION, &node_2_transition, &constant_node_narrative_2, do_nothing_array};

struct PlayerBasedTransition node_3_transition;
struct Narrative constant_node_narrative_3 = {"time to choose!"};
struct Node n_id_node_3 = {PLAYER_BASED_TRANSITION, &node_3_transition, &constant_node_narrative_3, do_nothing_array};

int always_false_predicate(struct GameState * game_state){
    return 0;
}

struct Branch always_false_branch = {&always_false_predicate, NULL_NODE_POINTER};

int always_true_predicate(struct GameState * game_state){
    return 1;
}
struct Branch node_1_branch = {&always_true_predicate, &n_id_node_3};
struct Branch * node_1_branches [] =  {&always_false_branch, &node_1_branch};
struct Branch node_2_branch = {&always_true_predicate, 0x00};
struct Branch * node_2_branches [] =  {&always_false_branch, &node_2_branch};

char * node_3_prompts[] = {"restart", "continue"};
struct Node * node_3_prompt_choices [] = {&n_id_node_1, &n_id_node_2};

void set_up_variables(){
    node_1_transition.branches = node_1_branches;
    node_1_transition.number_of_branches = 2;
    node_2_transition.branches = node_2_branches;
    node_2_transition.number_of_branches = 2;

    node_3_transition.node_choices = node_3_prompt_choices;
    node_3_transition.number_of_prompts = 2;
    node_3_transition.prompts = node_3_prompts;
}
#endif