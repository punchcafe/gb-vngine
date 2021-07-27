#include "../types/game_state.c"

void do_nothing(struct GameState * game_state){}
void(*do_nothing_array[1])(struct GameState *) = {&do_nothing};