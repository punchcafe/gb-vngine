#include "./functions/get_next_node.c"
#include "./functions/play_narrative.c"
#include "./types/game_state.c"
#include "./types/narrative.c"
#include "./types/node.c"
//DEV ONLY
#include "./gen/constant_next_node.c"
struct Node * current_node = &n_id_node_2;
struct GameState GLOBAL_GAME_STATE;
int main()
{
    while(current_node != 0x00)
    {
        current_node->game_state_modification(&GLOBAL_GAME_STATE);
        play_narrative(current_node);
        current_node = get_next_node(current_node);
    }
    return 1;
}