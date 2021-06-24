#include <stdio.h>
#include <gb/font.h>
#include <gb/console.h>
#include <gb/drawing.h>

#define NULL_NODE 0x00

struct GAMESTATE_GameState {
    int some_varible;
    int some_other_variable;
    char * some_str_var;
    int some_bool_var;
};
// can hard code, or implement map for GameState
// but initially, we don't need to support chapter variables

enum NODE_NodeType {
    STORY
};

struct NODE_Node {
    enum NODE_NodeType node_type;
    void * nodeBody;
    // Since all of this will be generate statically, we can use global references to game state within the 
    // methods themselves.
    struct NODE_Node* (*next_node_strategy)();
    void (*game_state_modification)(struct GAMESTATE_GameState * game_state);
};

struct NARRATIVE_Narrative {
    char * simple_speech_text;
};

struct NODE_StoryNodeBody {
    struct NARRATIVE_Narrative * narrative;
};

void NARRATIVE_read_narrative(struct NARRATIVE_Narrative * narrative){
    printf(narrative->simple_speech_text);
}



// probably useless 
void set_narrative_string(struct NARRATIVE_Narrative * narrative, char * str){
    narrative->simple_speech_text = str;
}

// NARRATIVES

struct NODE_Node * return_no_node(){
    return NULL_NODE;
}

void increment_some_var(struct GAMESTATE_GameState * game_state){
    game_state->some_varible++;
}

void no_game_state_modification(struct GAMESTATE_GameState * game_state){}

// forward declaration
struct NODE_Node * return_node_2();

struct NARRATIVE_Narrative n_id_node_1_narr = {"hello, world!"};
struct NODE_StoryNodeBody n_id_node_1_story_body = {&n_id_node_1_narr};
struct NODE_Node n_id_node_1 = {STORY, &n_id_node_1_story_body, &return_node_2, &increment_some_var};

struct NARRATIVE_Narrative n_id_node_2_narr = {"how are you?!"};
struct NODE_StoryNodeBody n_id_node_2_story_body = {&n_id_node_2_narr};
// NOTE!!
// use a hashing algo (based on the query string?? to not generate identical gsm modifiers). same can be 
// applied to predicate strings. use string as a map key, generate a file, retain meta data for generated predicates
// and create a map so that nodes can lookup their methods by expression
struct NODE_Node n_id_node_2 = {STORY, &n_id_node_2_story_body, &return_no_node, &increment_some_var};

/*
Iteration 1: doesn't support chapters, will validate only 1, and no chapter variables.
GB-vNGINE//Alpha 1.0
Iteration 2: support chapters
GB-vNGINE//Alpha 2.0
Iteration 3: support saving / loading
GB-vNGINE//Alpha 3.0
*/

struct NODE_Node * return_node_2(){
    return &n_id_node_2;
}

void handle_node(struct NODE_Node * node){
    if(node->node_type == STORY){
        struct NODE_StoryNodeBody* story_node_body = (struct NODE_StoryNodeBody*)(node->nodeBody);
        NARRATIVE_read_narrative(story_node_body->narrative);
    }
}

struct GAMESTATE_GameState GLOBAL_GAME_STATE = {0x00, 0x00, 0x00, 0x00};


int main() {
    //FONT SETUP
    font_t ibm_font, italic_font, min_font;

    /* First, init the font system */
    font_init();

    /* Load all the fonts that we can */
    ibm_font = font_load(font_ibm);  /* 96 tiles */
    italic_font = font_load(font_italic);   /* 93 tiles */
    
    /* Load this one with dk grey background and white foreground */
    color(WHITE, DKGREY, SOLID);
    
    min_font = font_load(font_min);

    /* Turn scrolling off (why not?) */
    mode(get_mode() | M_NO_SCROLL);

    /* Print some text! */
    
    /* IBM font */
    font_set(ibm_font);
    printf("Font demo.\n\n");
    //FONT SETUP
    struct NODE_Node * next_node = &n_id_node_1;
    while(next_node != NULL_NODE)
    {
        next_node->game_state_modification(&GLOBAL_GAME_STATE);
        handle_node(next_node);
        next_node = next_node->next_node_strategy();
    }
    printf("%d", GLOBAL_GAME_STATE.some_varible);
    return 1;
}