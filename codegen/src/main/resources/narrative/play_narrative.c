#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

#include <gb/gb.h>

void handle_foreground(struct ForegroundElement* foreground)
{
    switch (foreground->position)
    {
    case LEFT_PORTRAIT:
        set_character_tile_left(foreground->pattern);
        break;
    case RIGHT_PORTRAIT:
        set_character_tile_right(foreground->pattern);
        break;
    case CENTER_PORTRAIT:
        set_character_tile_center(foreground->pattern);
        break;
    case CENTER_FOCUS:
        set_focus_tile(foreground->pattern);
        break;
    default:
        break;
    }
}

void handle_text(struct Text* text)
{
    render_whole_text(text->text);
    await_a_button_press();
}

void handle_pause(struct Pause* pause)
{
    delay(pause->seconds_duration * 1000);
}

void handle_background(struct BackgroundElement * element){
    render_background(element);
}

void play_narrative_element(struct NarrativeElement *element)
{
    switch (element->type)
    {
    case TEXT:
        handle_text((struct Text*)element->content);
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
        handle_background((struct BackgroundElement*)element->content);
        break;
    default:
        break;
    }
}

void play_narrative(struct Narrative *narrative){
    for(int i = 0; i < narrative->number_of_elements; i++)
    {
        play_narrative_element(narrative->element_array[i]);
    }
}

#endif