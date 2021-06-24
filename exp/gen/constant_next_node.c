#ifndef CONSTANT_NEXT_NODE
#define CONSTANT_NEXT_NODE

#include <stdio.h>
#include "../types/narrative.c"
#include "../types/node.c"
#include "./game_state_mutations.c"
#include "../types/player_based_transition.c"
#include "../types/predicate_based_transition.c"

struct PredicateBasedTransition node_1_transition;
struct Narrative constant_node_narrative_1 = {"how are you, friend?!"};
struct Node n_id_node_1 = {PREDICATE_BASED_TRANSITION, &node_1_transition, &constant_node_narrative_1, &do_nothing};

struct PredicateBasedTransition node_2_transition;
struct Narrative constant_node_narrative_2 = {"I'm fine, and you?"};
struct Node n_id_node_2 = {PREDICATE_BASED_TRANSITION, &node_2_transition, &constant_node_narrative_2, &do_nothing};


int always_true_predicate(struct GameState * game_state){
    printf("i'm called!");
    return 1;
}
struct Branch node_1_branch = {&always_true_predicate, &n_id_node_2};
struct PredicateBasedTransition node_1_transition = {{&node_1_branch}, 1};
struct Branch node_2_branch = {&always_true_predicate, &n_id_node_1};
struct PredicateBasedTransition node_2_transition = {{&node_2_branch}, 1};
#endif