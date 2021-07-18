enum ActorMode { DIALOGUE_MODE, OBJECT_FOCUS_MODE } ACTOR_MODE;

#define DIALOGUE_PORTRAIT_SIZE 24
#define DIALOGUE_PORTRAIT_WIDTH 3
#define DIALOGUE_SPRITE_MODE // TODO

#define OBJECT_FOCUS_PORTRAIT_SIZE 48

#define LEFT_PORTRAIT_TILE_OFFSET 0
#define RIGHT_PORTRAIT_TILE_OFFSET 0

struct DailogueActorObject {
    char * patternTable;
    short patternTableSize;
    char spriteTable [DIALOGUE_PORTRAIT_SIZE];
};

struct ObjectFocusActorObject {
    char * patternTable;
    short patternTableSize;
    char spriteTable [OBJECT_FOCUS_PORTRAIT_SIZE];
};



void use_dialogue_actor_mode(){
    // Move all sprites into expect positions for left and right
    // start with EMPTY_TILE
};

void use_object_focus_actor_mode(){
    // Move all sprites into expect positions for left and right
    // start with EMPTY_TILE
};

void render_dialogue_mode_left(struct DailogueModeRenderObject * object)
{
    if(ACTOR_MODE != DIALOGUE_MODE) use_dialogue_actor_mode();
};
void render_dialogue_mode_right(struct DailogueModeRenderObject * object)
{
    if(ACTOR_MODE != DIALOGUE_MODE) use_dialogue_actor_mode();
    // Knows how to offset 
};
void render_object_focus(struct ObjectFocusActorObject object){
    if(ACTOR_MODE != DIALOGUE_MODE) use_dialogue_actor_mode();
}