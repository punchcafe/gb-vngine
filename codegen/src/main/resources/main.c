// TODO:

void game_mode()
{

}

// TODO: move to a async model for this as opposed to a blocking process

int main()
{
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
     modify_game_state(); // ignored if nothing to do
     play_narrative();
     play_music(); //
     get_next_node(); // ignored if nothing to do
     // TODO: get_next_node also alerts all subscribers. Will need to define subscriber functions in
     // main.c as opposed to context, so that functions can be bound to state singletons, and contexts remain clear

    }
 */