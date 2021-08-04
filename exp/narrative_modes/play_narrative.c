#include "./narrative.c"
#include "./narrative_elements/text.c"
#include "./text_renderer.c"

#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

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