#ifndef PLAY_NARRATIVE_DEFINITION
#define PLAY_NARRATIVE_DEFINITION

#include <gb/gb.h>

void handle_text(struct ExternalText * text)
{
    render_whole_text(text->text, text->bank);
    await_a_button_press();
}

void handle_pause(struct Pause* pause)
{
    delay_with_music(pause->seconds_duration * 60);
}

void handle_background(struct ExternalBackgroundAsset * element){
    render_background(element);
}

void handle_play_music(struct ExternalMusicAsset * element){
    play_music(element->music_data, element->bank_number, 7);
}

void play_narrative_element(struct NarrativeElement *element)
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

void play_narrative(struct Narrative *narrative){
    for(int i = 0; i < narrative->number_of_elements; i++)
    {
        play_narrative_element(narrative->element_array[i]);
    }
}

#endif