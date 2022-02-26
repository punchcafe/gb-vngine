package dev.punchcafe.gbvng.gen.render.sprites.prt;

import lombok.Builder;
import lombok.Getter;

/**
 * Wrapper Around a {@link PortraitAsset} which contains context about references to data blocks.
 * Should hash a name so isn't repeated. Will be useful in cases like rendering character responses and backgrounds
 * without duplicating the ForgroundElement
 *
 * Responsible for rendering individual Assets of type:
 * struct ForegroundElement
 * {
 *     unsigned short pattern_block_number;
 *     unsigned short bank_number;
 *     unsigned char * pattern_references;
 * };
 * which is in turn wrapped around a NarrativeElement.
 */
@Builder
@Getter
public class PortraitAssetCModel {

    private final int patternDataBlockNumber;
    // unused for now, to be implemented
    // private final int bank;
    private final PortraitAsset portraitAsset;
}
