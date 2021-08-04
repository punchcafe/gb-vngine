#ifndef NARRATIVE_TYPE_DEFINITION
#define NARRATIVE_TYPE_DEFINITION
enum NarrativeElementTypes {
    TEXT
};

struct NarrativeElement {
    void * content;
    enum NarrativeElementTypes type;
};

struct Narrative {
    int number_of_elements;
    struct NarrativeElement ** element_array;
};
#endif