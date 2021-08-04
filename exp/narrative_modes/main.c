#include <gb/gb.h>
#include "./narrative_elements/text.c"
#include "./narrative.c"
#include "./play_narrative.c"

struct Text text_to_play_1 = {"hello world i thought youd come"};
struct Text text_to_play_2 = {"i knew it i knew it i knew it"};

struct NarrativeElement text_elem_1 = {&text_to_play_1, TEXT};
struct NarrativeElement text_elem_2 = {&text_to_play_2, TEXT};

struct NarrativeElement * narratives [2] = {&text_elem_1, &text_elem_2};

struct Narrative narrative = {2, narratives};

int main()
{
    initialise_font();
    SHOW_BKG;
    play_narrative(&narrative);
}