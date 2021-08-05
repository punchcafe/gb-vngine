int main(){
    initialise_font();
    setup();
    SPRITES_8x16;
    SHOW_SPRITES;
    SHOW_BKG;
    while(current_node != GAME_OVER_NODE_ID)
    {
        current_node->game_state_modification[0](&GAME_STATE);
        play_narrative(current_node->narrative);
        current_node = get_next_node(current_node, &GAME_STATE);
    }
    return 1;
}