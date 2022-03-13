// EXPERIMENTAL

extern unsigned char music_asset_${ASSET_NAME}_data;

struct ExternalMusicAsset {
    unsigned int bank_number;
    unsigned char * music_data;
};

struct ExternalMusicAsset music_asset_${ASSET_NAME}_data {bank_num, };

// when rendering the ExternalMusicAsset, just use the external asset directly instead of a wrapper

void handle_play_music(struct ExternalMusicAsset * element){
    play_music(element->data, 1, 7);
}


void play_narrative_element(struct NarrativeElement *element)
{
    switch (element->type)
    {
..
    case PLAY_MUSIC:
        handle_play_music((struct ExternalMusicAsset*)element->content);
        break;
    }
}