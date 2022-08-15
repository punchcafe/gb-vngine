#ifndef PLAY_NARRATIVE_BINDINGS_DEF
#define PLAY_NARRATIVE_BINDINGS_DEF
struct DialogueBox _dialogue_box_base = {0,0};
struct DialogueBox * dialogue_box = &_dialogue_box_base;

struct PlayNarrativeDependencies _play_narrative_deps = {&_dialogue_box_base};
struct NarrativeState _play_narrative_narrative_state;


void play_narrative_bound(void (*observers[])(void),
                          unsigned short num_observers)
{
    play_narrative(&_play_narrative_narrative_state,
                            &_play_narrative_deps,
                            observers,
                            num_observers);
}
#endif