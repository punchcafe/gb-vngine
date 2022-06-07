#ifndef BACKGROUND_ELEMENT_DEFINITION
#define BACKGROUND_ELEMENT_DEFINITION

// TODO: separate into clean files

struct BackgroundAsset {
    unsigned char * background_tiles;
    unsigned short number_of_tiles;
    unsigned char * tile_assignements;
};

struct ExternalBackgroundAsset {
    struct BackgroundAsset * asset;
    unsigned short bank;
};

#endif