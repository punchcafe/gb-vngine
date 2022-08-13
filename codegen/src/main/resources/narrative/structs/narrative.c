#ifndef NARRATIVE_TYPE_DEFINITION
#define NARRATIVE_TYPE_DEFINITION
enum NarrativeElementTypes {
    TEXT, FOREGROUND, PAUSE, CLEAR_TEXT, BACKGROUND, PLAY_MUSIC
};

struct NarrativeElement {
    void * content;
    enum NarrativeElementTypes type;
};

struct Narrative {
    int number_of_elements;
    struct NarrativeElement ** element_array;
};
// TODO: create a temp one for persistence, like:
/*
struct Narrative save_point = {remaining_elem_number, remaining_elems};
potentially have different mechanism from launching from load.
*/
#endif