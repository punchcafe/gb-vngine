#ifndef GAMESTATE_TYPE_DEFINITION
#define GAMESTATE_TYPE_DEFINITION
struct GameState {

    // *** INTEGER VARIABLES *** 

    int some_int_1;
    int some_int_2;
    int some_int_3;


    // *** BOOLEAN VARIABLES ***

    short some_bool_var;
    short some_other_bool_var;


    // STRING VARIABLES

    char some_string_one [50]; // TODO: make a max value, or instead calculate max possible value?
    char some_string_two [50];

};
#endif