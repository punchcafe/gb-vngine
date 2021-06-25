#include "./defs/null_node.c"
#include "./functions/get_next_node.c"
#include "./functions/play_narrative.c"
#include "./types/game_state.c"
#include "./types/narrative.c"
#include "./types/node.c"
//DEV ONLY
#include "./gen/constant_next_node.c"
struct Node * current_node = &n_id_node_1;
struct GameState GLOBAL_GAME_STATE;
int main()
{
    set_up_variables();
    while(current_node != NULL_NODE_POINTER)
    {
        current_node->game_state_modification(&GLOBAL_GAME_STATE);
        play_narrative(current_node);
        current_node = get_next_node(current_node, &GLOBAL_GAME_STATE);
    }
    return 1;
}