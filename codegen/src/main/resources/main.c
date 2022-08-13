// TODO:

void game_mode()
{

}

// TODO: move to a async model for this as opposed to a blocking process


unsigned char _modify_game_state_done = 0x00;

// MODIFY GAMESTATE SECTION
// TODO: consider moving go gamestate context/binding
// TODO: extract
void modify_game_state()
{
    if(_modify_game_state_done){
        return;
    } else {
        current_node->game_state_modification[0](&GAME_STATE);
        _modify_game_state_done = 0x01;
    }
}

// PLAY_NARRATIVE_SECTION


struct NarrativeState narrative_state;

void play_narrative_bound()
{
    play_narrative(&narrative_state);
}



void get_next_node_bound()
{
    if(narrative_state.narrative_finished){
        // TODO: extract with proper binding
        current_node = get_next_node(current_node, &GAME_STATE);
         // TODO: extract to observer pattern
        _modify_game_state_done = 0x00;
        narrative_state.narrative_finished = 0x00;
        narrative_state_on_new_node(&narrative_state, current_node);
    }
    // TODO: update observers
}

void play_music_inc()
{
    gbt_update();
}

void game_mode_loop()
{
     modify_game_state();
     play_narrative_bound();
     play_music_inc();
     get_next_node_bound();
}

int main()
{
     setup();
     narrative_state_on_new_node(&narrative_state, current_node);
     SPRITES_8x16;
     SHOW_SPRITES;
     SHOW_BKG;
     while(current_node != GAME_OVER_NODE_ID)
     {
         wait_vbl_done();
         game_mode_loop();
     }
     return 1;
 }
 /*
 TODO: move to async could look like:

    _game_loop_state {
        current_narrative;

    }

    play_narrative():
      records current narrative element / type
      passes to element type. play_narrative then returns true or false if it finished or not
      if it finished, move the type along.
      When play narrative is entirely finished, it sets a flag _narrative_playing = false; which allows
      the other functions ( modify game state and get_next_node ) to do their jobs

    game_loop()
    {
    // TODO: tracks whether it's done it's transformation per node or not, and waits for node_change callback
    // to reset flag
     modify_game_state(); // ignored if nothing to do
     play_narrative();
     play_music(); //
     get_next_node(); // ignored if nothing to do
     // TODO: get_next_node also alerts all subscribers. Will need to define subscriber functions in
     // main.c as opposed to context, so that functions can be bound to state singletons, and contexts remain clear

    }
 */