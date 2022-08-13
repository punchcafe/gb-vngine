#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

#include <gb/gb.h>


struct NarrativeState {
    unsigned short current_element_index;
    struct Narrative * current_narrative;
    void * playing_element_state;
    // void pointer current element state
}

struct TextState {
    unsigned int current_character;
}

unsigned char handle_text(struct TextState * text)
{
    render_whole_text(text->text, text->bank);
    await_a_button_press();
}

unsigned char handle_pause(struct Pause* pause)
{
    delay_with_music(pause->seconds_duration * 60);
}

unsigned char handle_background(struct ExternalBackgroundAsset * element){
    render_background(element);
}

unsigned char handle_play_music(struct ExternalMusicAsset * element){
    play_music(element->music_data, element->bank_number, 7);
}

// TODO: Returns boolean
unsigned char play_narrative_element(struct NarrativeElement *element)
{
    switch (element->type)
    {
    case TEXT:
        handle_text((struct ExternalText*)element->content);
        break;
    case FOREGROUND:
        handle_foreground((struct ForegroundElement*)element->content);
        break;
    case CLEAR_TEXT:
        clear_text_box();
        break;
    case PAUSE:
        handle_pause((struct Pause*)element->content);
        break;
    case BACKGROUND:
        handle_background((struct ExternalBackgroundAsset*)element->content);
        break;
    case PLAY_MUSIC:
        handle_play_music((struct ExternalMusicAsset*)element->content);
        break;
    default:
        break;
    }
}

unsigned char _narrative_finished = 0x00;

// NOT implementing observer function. This will be bound by a function in main.c
void play_narrative_on_node_change(struct NarrativeState *narrative_state, struct Node * node)
{
    // TODO:
    /*
    1. check if non-null narrative
    2. if non-null, set _narrative_finished to false, and set the narrative state
    */
}

void play_narrative(struct NarrativeState *narrative_state){
// TODO: this doesn't allow for setting of the narrative, since that has to happen in reaction
// TODO: possibly have 'handle node change' callbacks?
    if (_narrative_finished)
    {
        return;

    }

    struct NarrativeElement * current_element = narrative_state->current_narrative->element_array[narrative_state->current_element];
    unsigned char element_finished? = play_narrative_element(narrative_state->current_element);
    if(element_finished?)
    {
        unsigned char narrative_finished? = narrative_state->current_narrative->number_of_elements >= (narrative_state->current_element_index + 1);
        if(narrative_finished?){
            _narrative_finished = 0x01;
        } else {
            narrative_state->current_element_index += 1;
            // potentially set fresh state here on the narrative_state element
        }
    }
}

#endif