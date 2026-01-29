

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
int main()
{
    return 0;
}