#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

#include <gb/gb.h>

struct PlayNarrativeDependencies {
    struct DialogueBox * box;
    struct TextNarrativeState * text_narrative_state;
    struct BackgroundNarrativeState * background_narrative_state;
    struct ForegroundNarrativeState * foreground_narrative_state;
};

struct NarrativeState {
    unsigned short current_element_index;
    struct Narrative * current_narrative;
    unsigned char element_is_initialised;
    void * playing_element_state;
    // void pointer current element state
    int narrative_finished;
};

// ####### BACKGROUND_RENDERING_START

#define BACKGROUND_WIDTH 20
#define BACKGROUND_HEIGHT 12
#define BACKGROUND_TILE_AREA BACKGROUND_HEIGHT * BACKGROUND_WIDTH
#define BACKGROUND_START_Y_INDEX 0
#define BACKGROUND_START_X_INDEX 0

struct BackgroundNarrativeState {
    unsigned int render_offset;
};

// TURN OFF EVERYTHING UNTIL REDNDER IS DONE

#define BKG_TILES_PER_VBLANK 7

int play_narrative_render_background(struct ExternalBackgroundAsset * current_asset,
                                              struct BackgroundNarrativeState * state)
{
    SWITCH_ROM_MBC1(current_asset->bank);
    struct BackgroundAsset * element = current_asset->asset;
    unsigned int i = state->render_offset;
    unsigned int limit = val_or_clamp(i + BKG_TILES_PER_VBLANK, BACKGROUND_TILE_AREA);
    while(i < limit)
    {
        // TODO: optimise so that we build the full ref array in the state
        // and then set_bkg_tiles only once.
        unsigned int x = i % BACKGROUND_WIDTH;
        unsigned int y = i / BACKGROUND_WIDTH;
        unsigned char assignment_block [] = {element->tile_assignements[i] + TEXT_PATTERNS_END_INDEX + 1};
        set_bkg_tiles(x, y, 1, 1, assignment_block);
        i++;
    }
    state->render_offset = limit;
    if(limit >= BACKGROUND_TILE_AREA)
    {
        SHOW_BKG;
        SHOW_SPRITES;
        return 1;
    }
    return 0;
}

void play_narrative_init_background(struct ExternalBackgroundAsset * asset,
                                    struct PlayNarrativeDependencies * dependencies)
{
    HIDE_BKG;
    HIDE_SPRITES;
    SWITCH_ROM_MBC1(asset->bank);
    set_bkg_data(TEXT_PATTERNS_END_INDEX + 1, asset->asset->number_of_tiles, asset->asset->background_tiles);
    dependencies->background_narrative_state->render_offset = 0;
}

// ####### BACKGROUND_RENDERING_END

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

int handle_text(struct ExternalText * text,
                struct DialogueBox * dialogue_box,
                struct TextNarrativeState * text_narrative_state)
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
        return handle_text((struct ExternalText*)element->content,
                            dependencies->box,
                            dependencies->text_narrative_state);
    case FOREGROUND:
         return handle_foreground((struct ForegroundElement*)element->content, dependencies->foreground_narrative_state);
    case CLEAR_TEXT:
        dialogue_box_clear_screen(dependencies->box);
        return 0x01;
/*
    case PAUSE:
        return handle_pause((struct Pause*)element->content);
        */
    case BACKGROUND:
        return play_narrative_render_background((struct ExternalBackgroundAsset*)element->content,
                                                dependencies->background_narrative_state);
    case PLAY_MUSIC:
        return handle_play_music((struct ExternalMusicAsset*)element->content);
    default:
        return 0x01;
    }
}

void play_narrative_init_render_foreground(struct ForegroundElement* foreground_element,
struct ForegroundNarrativeState * render_state)
{
    render_state->tile_offset = 0;
    render_state->await_screen_acc = 0;
    render_state->awaiting_screen = 0x00;
    HIDE_SPRITES;
    SWITCH_ROM_MBC1(foreground_element->asset->bank_number);
    set_pattern_block(foreground_element->asset->asset->block);
    enum ForegroundElementPosition next_position = foreground_element->position;
    render_state->must_move_sprites = next_position != render_state->previous_sprite_position;
    render_state->previous_sprite_position = next_position;
}

void init_narrative_element(struct NarrativeElement *element, struct PlayNarrativeDependencies * dependencies)
{
    switch (element->type)
    {
    case BACKGROUND:
        play_narrative_init_background((struct ExternalBackgroundAsset*)element->content, dependencies);
        return;
    case FOREGROUND:
        play_narrative_init_render_foreground(
                (struct ForegroundElement*)element->content,
                dependencies->foreground_narrative_state);
        return;
    default:
        return;
    }
}

// NOT implementing observer function. This will be bound by a function in main.c
void play_narrative_on_node_change(struct NarrativeState *narrative_state, struct Node * node)
{
    narrative_state->current_element_index = 0;
    narrative_state->narrative_finished = 0;
    narrative_state->current_narrative = node->narrative;
}

void play_narrative(struct NarrativeState *narrative_state,
                    struct PlayNarrativeDependencies * dependencies,
                    void (*observers[])(void),
                    unsigned short num_observers){
    if (narrative_state->narrative_finished)
    {
        return;
    }

    struct NarrativeElement * current_element = narrative_state->current_narrative->element_array[narrative_state->current_element_index];
    if(!narrative_state->element_is_initialised)
    {
        init_narrative_element(current_element, dependencies);
        narrative_state->element_is_initialised = 0x01;
    }
    int element_finished = play_narrative_element(current_element, dependencies);
    if(element_finished)
    {
        narrative_state->element_is_initialised = 0x00;
        int narrative_finished = narrative_state->current_narrative->number_of_elements <= (narrative_state->current_element_index + 1);
        if(narrative_finished){
            narrative_state->narrative_finished = 0x01;
            for(int i = 0; i < num_observers; i++)
            {
                observers[i]();
            }
        } else {
            narrative_state->current_element_index += 1;
        }
    }
}

#endif