#ifndef PLAY_NARRATIVE_BINDINGS_DEF
#define PLAY_NARRATIVE_BINDINGS_DEF
struct DialogueBox _dialogue_box_base = {0,0};
struct DialogueBox * dialogue_box = &_dialogue_box_base;

//TODO: ensure all initializations work

struct TextNarrativeState text_narrative_state_base = {0x00, 0, 0};
struct TextNarrativeState * text_narrative_state = &text_narrative_state_base;

struct BackgroundNarrativeState background_narrative_state_base = {0x00};
struct BackgroundNarrativeState * background_narrative_state = &background_narrative_state_base;

struct ForegroundNarrativeState foreground_narrative_state_base = {0x00};
struct ForegroundNarrativeState * foreground_narrative_state = &foreground_narrative_state_base;

struct PlayNarrativeDependencies _play_narrative_deps = {&_dialogue_box_base,
&text_narrative_state_base,
&background_narrative_state_base,
&foreground_narrative_state_base};
struct NarrativeState _play_narrative_narrative_state;


void play_narrative_bound(void (*observers[])(void),
                          unsigned short num_observers)
{
    play_narrative(&_play_narrative_narrative_state,
                            &_play_narrative_deps,
                            observers,
                            num_observers);
}

void play_narrative_node_change_observer(struct Node * node)
{
    play_narrative_on_node_change(&_play_narrative_narrative_state, node);
}
#endif