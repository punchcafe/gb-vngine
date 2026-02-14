

struct GameState {
    unsigned char aBool;
    char * a_string;
    int counter;

};

#include <string.h>
typedef unsigned char bool;

typedef void (*GameStateModification)(struct GameState*);
typedef bool (*GameStatePredicate)(struct GameState*);

#define true 1
#define false 0

enum NodeTransitionType {
    PLAYER_BASED_TRANSITION,
    PREDICATE_BASED_TRANSITION
};

typedef struct Node {
    enum NodeTransitionType node_transition_type;
    void * node_transition_object;
    // struct Narrative * narrative;
    GameStateModification * game_state_modifications;
    short number_of_gsm;
};
int main()
{
    return 0;
}

#define STRING_REG_1 "some constant value"
#define STRING_REG_2 "new_value"

void MUTATION_ADD_is_VAR_counter_is_0(struct GameState * game_state){
  game_state->counter += 0;
}
void MUTATION_SET_is_VAR_aBool_is_true(struct GameState * game_state){
  game_state->aBool = true;
}
void MUTATION_SET_is_VAR_a_string_is_STRING_REG_2(struct GameState * game_state){
  game_state->a_string = STRING_REG_2;
}
void MUTATION_SET_is_VAR_counter_is_0(struct GameState * game_state){
  game_state->counter = 0;
}

bool is_VAR_a_string_EQUALS_STRING_REG_1(struct GameState * game_state) {
	return (strcmp(game_state->a_string, STRING_REG_1) == 0);
}
bool is_1_EQUALS_1(struct GameState * game_state) {
	return 1 == 1;
}