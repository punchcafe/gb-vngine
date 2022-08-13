#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

#include <gb/gb.h>


struct NarrativeState {
    unsigned short current_element_index;
    struct Narrative * current_narrative;
    void * playing_element_state;
    // void pointer current element state
    int narrative_finished;
};

void narrative_state_on_new_node(struct NarrativeState * state, struct Node * node)
{
    state->current_element_index = 0;
    state->narrative_finished = 0;
    state->current_narrative = node->narrative;
}

struct TextState {
    unsigned int current_character;
};

int handle_text(struct ExternalText * text)
{
    render_whole_text(text->text, text->bank);
    await_a_button_press();
    return 0x01;
}

int handle_pause(struct Pause* pause)
{
    delay_with_music(pause->seconds_duration * 60);
    return 0x01;
}

int handle_background(struct ExternalBackgroundAsset * element){
    render_background(element);
    return 0x01;
}

int handle_play_music(struct ExternalMusicAsset * element){
    play_music(element->music_data, element->bank_number, 7);
    return 0x01;
}

// TODO: Returns boolean
int play_narrative_element(struct NarrativeElement *element)
{
    switch (element->type)
    {
    case TEXT:
        return handle_text((struct ExternalText*)element->content);
        /*
    case FOREGROUND:
        return handle_foreground((struct ForegroundElement*)element->content);
    case CLEAR_TEXT:
        return clear_text_box();
    case PAUSE:
        return handle_pause((struct Pause*)element->content);
    case BACKGROUND:
        return handle_background((struct ExternalBackgroundAsset*)element->content);
        */
    case PLAY_MUSIC:
        return handle_play_music((struct ExternalMusicAsset*)element->content);
    default:
        return 0x01;
    }
}

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
    if (narrative_state->narrative_finished)
    {
        return;
    }

    struct NarrativeElement * current_element = narrative_state->current_narrative->element_array[narrative_state->current_element_index];
    int element_finished = play_narrative_element(current_element);
    if(element_finished)
    {
        int narrative_finished = narrative_state->current_narrative->number_of_elements <= (narrative_state->current_element_index + 1);
        if(narrative_finished){
            narrative_state->narrative_finished = 0x01;
        } else {
            narrative_state->current_element_index += 1;
            // potentially set fresh state here on the narrative_state element
        }
    }
}

#endif