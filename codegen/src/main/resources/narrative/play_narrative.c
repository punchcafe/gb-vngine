#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

#include <gb/gb.h>

struct PlayNarrativeDependencies {
    struct DialogueBox * box;
};

struct NarrativeState {
    unsigned short current_element_index;
    struct Narrative * current_narrative;
    void * playing_element_state;
    // void pointer current element state
    int narrative_finished;
};

struct TextNarrativeState text_narrative_state_base = {0x00, 0, 0};
struct TextNarrativeState * text_narrative_state = &text_narrative_state_base;

void narrative_state_on_new_node(struct NarrativeState * state, struct Node * node)
{
    state->current_element_index = 0;
    state->narrative_finished = 0;
    state->current_narrative = node->narrative;
}

struct TextNarrativeState {
    unsigned char awaiting_press;
    unsigned char finished;
    unsigned int text_offset;
    unsigned short vblank_waited;
};

#define TEXT_NARRATIVE_STATE_VBLANKS_PER_CHAR 3
#define TEXT_NARRATIVE_STATE_VBLANKS_PER_A_PRESS 25

void text_narrative_state_reset(struct TextNarrativeState * state)
{
    state->awaiting_press = 0x00;
    state->text_offset = 0x00;
    state->vblank_waited = 0x00;
    state->finished = 0x00;
}

int next_word_length(unsigned char * str)
{
    unsigned char position = 0;
    unsigned char character = str[position];
    while(character != ' ' && character != '\n' && character != '\0'){
        position++;
        character = str[position];
    }
    return position;
}

int handle_text(struct ExternalText * text, struct DialogueBox * dialogue_box)
{
    if(text_narrative_state->finished && joypad() & J_A)
    {
        text_narrative_state_reset(text_narrative_state);
        dialogue_box_clear_screen(dialogue_box);
        return 1;
    }
    //TODO: on blank space, lookahead to see
    text_narrative_state->vblank_waited++;
    if(text_narrative_state->awaiting_press)
    {
        if(joypad() & J_A){
            dialogue_box_clear_screen(dialogue_box);
            text_narrative_state->awaiting_press = 0x00;
            text_narrative_state->vblank_waited = 0;
            return 0;
        }
        if(text_narrative_state->vblank_waited > TEXT_NARRATIVE_STATE_VBLANKS_PER_A_PRESS)
        {
            dialogue_box_toggle_a_button(dialogue_box);
            text_narrative_state->vblank_waited = 0;
        }
        return 0;
    } else {
        if(text_narrative_state->vblank_waited > TEXT_NARRATIVE_STATE_VBLANKS_PER_CHAR){
            text_narrative_state->vblank_waited = 0;
            SWITCH_ROM_MBC1(text->bank);
            unsigned char character = text->text[text_narrative_state->text_offset];
            if(character == '\0')
            {
                text_narrative_state->finished = 0x01;
                text_narrative_state->awaiting_press = 0x01;
                return 0;
            }
            if(dialogue_box_cursor_increment(dialogue_box))
            {
                if(character == ' ')
                {
                    unsigned char * offseted_str = text->text + text_narrative_state->text_offset + 1;
                    if(next_word_length(offseted_str) > dialogue_box_chars_left_on_row(dialogue_box)){
                        dialogue_box_new_line(dialogue_box);
                    }
                }
                text_narrative_state->text_offset++;
                dialogue_box_print_char(dialogue_box, character);
                return 0;
            } else {
                text_narrative_state->awaiting_press = 0x01;
                return 0;
            }
        }
    }
    return 0;
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
int play_narrative_element(struct NarrativeElement *element, struct PlayNarrativeDependencies * dependencies)
{
    switch (element->type)
    {
    case TEXT:
        return handle_text((struct ExternalText*)element->content, dependencies->box);
        /*
    case FOREGROUND:
        return handle_foreground((struct ForegroundElement*)element->content);
        */
    case CLEAR_TEXT:
        dialogue_box_clear_screen(dependencies->box);
        return 0x01;
/*
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

void play_narrative(struct NarrativeState *narrative_state,
                    struct PlayNarrativeDependencies * dependencies,
                    void (*observers[])(void),
                    unsigned short num_observers){
// TODO: this doesn't allow for setting of the narrative, since that has to happen in reaction
// TODO: possibly have 'handle node change' callbacks?
    if (narrative_state->narrative_finished)
    {
        return;
    }

    struct NarrativeElement * current_element = narrative_state->current_narrative->element_array[narrative_state->current_element_index];
    int element_finished = play_narrative_element(current_element, dependencies);
    if(element_finished)
    {
        int narrative_finished = narrative_state->current_narrative->number_of_elements <= (narrative_state->current_element_index + 1);
        if(narrative_finished){
            narrative_state->narrative_finished = 0x01;
            for(int i = 0; i < num_observers; i++)
            {
                observers[i]();
            }
        } else {
            narrative_state->current_element_index += 1;
            // potentially set fresh state here on the narrative_state element
        }
    }
}

#endif