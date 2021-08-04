#include "./narrative.c"
#include "./narrative_elements/text.c"
#include "./text_renderer.c"
#include "./foreground.c"
#include "./narrative_elements/foreground_element.c"

#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

void handle_foreground(struct ForegroundElement* foreground)
{
    switch (foreground->position)
    {
    case LEFT:
        set_character_tile_left(foreground->pattern);
        break;
    case RIGHT:
        set_character_tile_right(foreground->pattern);
        break;
    case CENTER:
        set_focus_tile(foreground->pattern);
        break;
    default:
        break;
    }
}

void handle_text(struct Text* text){
    render_whole_text(text->text);
    await_a_button_press();
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