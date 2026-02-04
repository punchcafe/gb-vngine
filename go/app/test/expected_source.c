

struct GameState {
    unsigned char aBool;
    char * a_string;
    int counter;

};

typedef unsigned char bool;

typedef void (*GameStateModification)(struct GameState*);
typedef bool (*GameStatePredicate)(struct GameState*);

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

#define STRING_REG_1 "some constant value"

bool is_VAR_a_string_EQUALS_STRING_REG_1(struct GameState * game_state) {
	return str_compare(game_state->a_string, STRING_REG_1);
}
bool is_1_EQUALS_1(struct GameState * game_state) {
	return 1 == 1;
}
int main()
{
    return 0;
}