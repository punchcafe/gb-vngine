#define BACKGROUND_TILE_SIZE
#define BACKGROUND_TILE_WIDTH
#define BACKGROUND_TILE_HEIGHT

struct BackgroundActorObject {
    char * patternTable;
    short patternTableSize;
    char spriteTable [BACKGROUND_TILE_SIZE];
};

void render_background(struct BackgroundActorObject backgroundObject);