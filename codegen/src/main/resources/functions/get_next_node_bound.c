struct GetNextNodeState {

};

struct Node * _current_node;

void maybe_get_next_node(struct GameState * gs, /*void pointer callback list*/ observers);

struct Node * current_node();

