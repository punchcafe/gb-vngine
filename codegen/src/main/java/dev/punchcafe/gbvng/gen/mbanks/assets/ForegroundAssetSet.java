package dev.punchcafe.gbvng.gen.mbanks.assets;

import dev.punchcafe.gbvng.gen.mbanks.BankableAssetBase;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PatternBlock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * A forground asset set encompasses a set of foreground assets which share a common pattern table.
 * All foreground assets must belong to a foreground asset set, even if one is not specified in the configuration.
 * In the case of one not being specified, a Set will be created for a single asset.
 */
@Builder(toBuilder = true)
public class ForegroundAssetSet extends BankableAssetBase {

    /**
     * Current style to move away from:
     * const unsigned char foreground_asset_pattern_block_0_data [] = {0x7d,0xff,0xbd,0xff,0xfd,0xff,0xfd,0xff,0xfd,0xff,0xf9,0xff,0xf9,0xff,0xf9,0xff,0xfb,0xff,0x3b,0xff,0x0b,0x3f,0x03,0x06,0x03,0x07,0x03,0x07,0x03,0x07,0x03,0x07,0xe7,0x18,0xff,0x00,0xdf,0x20,0xfb,0x1c,0xdf,0x20,0xff,0x00,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0xbb,0xff,0xc7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x7f,0xff,0xff,0xff,0x7f,0x7f,0xff,0xbf,0xff,0xdf,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x02,0x03,0x01,0x01,0x02,0x03,0x01,0x01,0x02,0x03,0x01,0x01,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0xc0,0xc0,0xc0,0xc0,0x27,0xe7,0xef,0x3f,0xff,0x01,0xe3,0x1f,0xaf,0x7f,0x7e,0xff,0xbf,0xdf,0xde,0xaf,0x8f,0xff,0xff,0xff,0xff,0xff,0x01,0xff,0x01,0xff,0x55,0xab,0xab,0x55,0xff,0x01,0xff,0x1f,0xff,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0xc0,0xc0,0xe0,0xe0,0xf8,0xf8,0xfc,0xfc,0xff,0xff,0xff,0xff,0xfe,0xff,0xfd,0xfe,0xe0,0xff,0x1f,0xe0,0xf8,0x07,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xff,0x9f,0xff,0xbf,0xff,0xff,0xde,0xff,0xbe,0xcf,0x9e,0xe7,0x0f,0xf7,0x0e,0xf7,0x36,0xcf,0x7c,0xbf,0x7c,0xff,0x7e,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xff,0xff,0xe7,0x18,0x7e,0x81,0xff,0xff,0xd5,0xff,0xeb,0x56,0xc3,0x3c,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0xf8,0xfe,0xfc,0xfe,0x7e,0x7e,0xfe,0xfe,0xfe,0x01,0x54,0xab,0x6b,0xd5,0x94,0xeb,0xeb,0xd5,0x94,0xeb,0xeb,0xff,0xd7,0xff,0xfe,0xfe,0x06,0xfe,0x0e,0xf6,0xfc,0x04,0xff,0xfd,0xff,0xff,0xf3,0xff,0xe1,0xff,0xf9,0xe7,0xf5,0xcb,0xe9,0xd7,0xc1,0xbf,0xe9,0xd7,0xf1,0xef,0xfd,0xf3,0xfd,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbb,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0xff,0xff,0xff,0xaf,0xdf,0xef,0xff,0xdf,0xfe,0xaf,0xdf,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0xff,0x00,0xc3,0x3c,0xff,0x7e,0xff,0x7e,0xc3,0x7e,0xc3,0x3c,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0x70,0xff,0xe3,0xff,0xc3,0xff,0x99,0xe7,0xdf,0xe3,0x99,0xe7,0xfb,0xc7,0xff,0x00,0xff,0x00,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0xf7,0xff,0xe7,0xff,0x00,0xff,0x99,0xe7,0xdf,0xe3,0x99,0xe7,0xfb,0xc7,0xff,0x00,0xff,0x00,0xff,0x00,0xc3,0x3c,0xe7,0x18,0xbd,0xff,0xff,0x00,0xe7,0x18,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x1f,0x1f,0x0d,0x0e,0x07,0x07,0x03,0x03,0x01,0x01,0x00,0x00,0xbe,0xff,0x26,0xff,0x6c,0xff,0xe8,0xff,0xe8,0xff,0xf0,0xff,0xf0,0xff,0xf9,0xff,0xfd,0xff,0xff,0xff,0xe0,0xfe,0x60,0xfe,0x40,0xfc,0x40,0xf8,0x40,0xf0,0x10,0xf0,0xf6,0x0e,0x6c,0x9c,0x68,0x98,0xd0,0x30,0xb0,0x70,0x7c,0xfc,0xde,0xe6,0x8d,0xf3,0xd5,0xeb,0x8d,0xf3,0xcf,0xf0,0xc7,0xf9,0xce,0xf3,0xdf,0xe3,0xbf,0xc7,0xbf,0xcf,0x87,0xff,0x07,0xff,0x87,0xff,0x83,0xff,0x13,0xef,0x21,0xdf,0x10,0xef,0x28,0xd7,0x55,0xaa,0xaa,0x55,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x00,0x00,0x80,0x80,0x00,0x00,0x80,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xf6,0x0e,0xec,0x1c,0xe8,0x18,0xd0,0x30,0xb0,0x70,0x7c,0xfc,0xde,0xe6,0x8d,0xf3,0xd5,0xeb,0x8d,0xf3,0xcf,0xf0,0xc7,0xf9,0xce,0xf3,0xdf,0xe3,0xbf,0xc7,0xbf,0xcf,0xff,0x7f,0xbf,0xff,0xfe,0x7f,0xbe,0xff,0xfc,0x7f,0xb9,0xff,0xf9,0x7f,0xbb,0xff,0x79,0xff,0xbc,0xff,0x7e,0xff,0xbf,0xff,0x7f,0xff,0xbf,0xff,0x7f,0xff,0xbf,0xff,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x03,0x03,0x0f,0x0f,0x1f,0x1f,0x3f,0x3f,0x07,0x07,0x03,0x03,0x07,0x07,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xff,0x87,0xff,0xc7,0xff,0x07,0xff,0x7e,0xff,0xbf,0xdf,0xdf,0xaf,0x8f,0xff,0xff,0xff,0xff,0xff,0x00,0xff,0x00,0xff,0x55,0xaa,0xaa,0x55,0xff,0x01,0xff,0x1f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xc0,0xc0,0x80,0x80,0xc0,0xc0,0x80,0x80,0x00,0x00,0x00,0x00,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0xff,0xff,0xff,0x3f,0xdf,0xef,0xff,0xdf,0xfe,0xaf,0xdf,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0x70,0xff,0xe3,0xff,0xe7,0xff,0x99,0xe7,0xde,0xe3,0xd8,0xe7,0xfb,0xc7,0xff,0x00,0xe7,0x18,0xfc,0xfc,0xf8,0xfc,0xe8,0xfc,0xe8,0xfc,0xe8,0xfc,0xf0,0xfc,0xf0,0xfc,0xf8,0xfc,0xf8,0xfc,0xfe,0xfe,0xfc,0xfe,0xfc,0xfe,0xf8,0xfe,0xf8,0xfe,0xf8,0xfe,0xf8,0xfe,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x0f,0x0f,0xff,0xff,0xf7,0x0f,0x31,0xcf,0xfc,0xc3,0xe3,0xfc,0x7f,0x7c,0xff,0x7f,0xbf,0xff,0xff,0x7f,0xbf,0xff,0xff,0x7f,0xbf,0xff,0xff,0x7f,0xbf,0xff,0x7f,0xff,0xbf,0xff,0x7f,0xff,0xbe,0xff,0x7e,0xff,0xbe,0xff,0x7e,0xff,0xbe,0xff,0xef,0xb0,0xf6,0xf9,0x16,0x19,0x0b,0x0c,0x1d,0x1e,0x36,0x3f,0x7f,0x6f,0x73,0x4f,0xf5,0x8f,0xdb,0xa7,0x83,0x7f,0xf5,0x0b,0xa9,0xd7,0xf1,0xef,0xf9,0xf7,0xfd,0xfb,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x3f,0xff,0xff,0xff,0xbf,0xdf,0xef,0xff,0xdf,0xfe,0xaf,0xdf,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,0x00,0x00,0x03,0x03,0x0f,0x0f,0x1f,0x1f,0x15,0x1f,0x0a,0x0f,0x0f,0x0f,0x0f,0x0f,0x07,0x07,0x03,0x07,0x01,0x03,0x01,0x01,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xf8,0xf8,0xf8,0xf8,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xf7,0xff,0xf1,0xff,0x77,0xff,0xbb,0xff,0xf5,0xfb,0x7b,0xff,0xaf,0xf0,0xef,0x70,0x7f,0xff,0x08,0xff,0xb3,0xcf,0x5f,0xf5,0x3f,0xf9,0x13,0xfd,0x12,0xff,0x13,0xff,0xff,0x7f,0xb5,0xdf,0xb7,0xdf,0xbf,0xc7,0x9f,0xe7,0x8f,0xf7,0x87,0xff,0x87,0xff,0x00,0xe0,0x60,0xe0,0xe0,0xe0,0xc0,0xc0,0xc0,0xc0,0xc0,0xc0,0x40,0xc0,0x40,0xc0,0x40,0xc0,0xc0,0x40,0xc0,0xc0,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xf7,0xff,0xf9,0xff,0x77,0xff,0xb9,0xff,0xf5,0xfa,0x7b,0xff,0xaf,0xf0,0xef,0x70,0xef,0xff,0xff,0xe7,0xf7,0xef,0xe7,0xff,0xff,0xff,0xff,0xff,0x00,0xff,0x00,0xff,0x55,0xaa,0xaa,0x55,0xff,0x00,0xff,0x00,0xff,0xf0,0xff,0xff,0xff,0xff,0xff,0xff,0x1f,0x1f,0x0f,0x1f,0x3f,0x3f,0x3b,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x7d,0x7f,0x7d,0x7f,0x7d,0x7f,0x5c,0x7f,0x5c,0x7f,0x5c,0x7f,0x7d,0x7f,0x3c,0x7f,0x03,0x07,0x07,0x07,0x07,0x07,0x05,0x07,0x03,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x03,0x03,0x03,0x03,0x01,0x01,0x01,0x01,0x7d,0x7f,0x7d,0x7f,0x7d,0x7f,0xbd,0xff,0xfd,0xff,0xf9,0xff,0xf9,0xff,0xf9,0xff,0xfb,0xff,0xfb,0xff,0xfb,0xff,0xfb,0xfe,0xfb,0xff,0xfb,0xff,0xfb,0xff,0xfb,0xff,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xef,0xb0,0xf7,0xf8,0x17,0x18,0x0b,0x0c,0x1d,0x1e,0x36,0x3f,0x7f,0x6f,0x73,0x4f,0xf5,0x8f,0xdb,0xa7,0x83,0x7f,0xf5,0x0b,0xa9,0xd7,0xf1,0xef,0xf9,0xf7,0xfd,0xfb,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0x70,0xff,0xc3,0xff,0xe7,0xff,0x81,0xff,0xdf,0xe3,0x99,0xe7,0xfb,0xc7,0xe7,0x18,0xe7,0x18,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0xff,0xff,0xff,0xbf,0xdf,0xef,0xff,0xff,0xfe,0xef,0x7f,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0x80,0x80,0x00,0x00,0x00,0x00,0x80,0x80,0x80,0x80,0xe0,0xe0,0xf0,0xf0,0xe0,0xe0,0x80,0x80,0x80,0x80,0xc0,0xc0,0xe0,0xe0,0x80,0x80,0x80,0x80,0x80,0x80,0x00,0x00,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfd,0xff,0xf7,0xff,0xf8,0xff,0x77,0xff,0xbb,0xff,0xf5,0xfb,0x7b,0xff,0xaf,0xf0,0xef,0x70,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x80,0x80,0x00,0x80,0xc0,0xc0,0xf0,0xf0,0xf8,0xf8,0xf8,0xf8,0x1f,0x1f,0x0f,0x1f,0x3f,0x3f,0x3b,0x3f,0x3f,0x3f,0xff,0xff,0xff,0x7f,0xff,0x3f,0x3d,0xff,0x3d,0xff,0xbd,0xff,0x9c,0xff,0x9c,0x7f,0xdc,0xff,0x1d,0xff,0xbc,0xff,0xf3,0xff,0xff,0xff,0xff,0xff,0xfd,0xff,0xff,0x03,0x56,0xab,0xaa,0x5f,0x56,0xaf,0xae,0x5f,0x56,0xff,0xae,0xff,0xfe,0xff,0xc3,0xff,0x83,0xff,0xfd,0x83,0x7f,0x41,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xf7,0xff,0xf9,0xff,0x77,0xff,0xbb,0xff,0xf5,0xfb,0x7b,0xff,0xaf,0xf0,0xef,0x70,0x00,0x00,0x00,0x00,0x00,0x00,0x06,0x06,0x0f,0x09,0x0f,0x08,0x1d,0x12,0x3d,0x32,0x7d,0x72,0x6f,0x52,0x4f,0x72,0x4f,0x70,0x4f,0x7e,0x31,0x2f,0x30,0x2f,0x1a,0x1f};
     * const unsigned char foreground_asset_pattern_block_1_data [] = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x09,0x0f,0x0b,0x0f,0x13,0x1f,0x27,0x3f,0x4f,0x7f,0x9e,0xff,0xbc,0xff,0x3c,0xff,0x38,0xff,0x38,0xff,0x3c,0xff,0x8f,0xff,0x78,0x7f,0x38,0x3f,0x00,0x0f,0x00,0x17,0xf3,0xff,0xf1,0xff,0xf0,0xff,0x70,0xff,0x78,0xff,0x78,0xff,0x7a,0xff,0x7c,0xff,0x3f,0xff,0x3f,0xff,0x1f,0xff,0x0f,0xff,0x07,0xff,0x07,0xff,0x01,0xff,0x00,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x36,0xff,0x66,0xff,0xc6,0xff,0x96,0xff,0x0e,0xff,0xae,0xff,0x0e,0xff,0x57,0xff,0xaf,0xff,0x57,0xff,0xab,0xff,0x55,0xff,0xaa,0xff,0x01,0xfd,0x00,0xec,0x00,0xbc,0x00,0xf8,0x00,0x38,0x00,0x38,0x00,0x18,0x00,0x18,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x74,0x7c,0x7c,0x7c,0x70,0x70,0x30,0xb0,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x40,0x00,0x60,0x00,0xe0,0x00,0xf0,0x00,0xf0,0x00,0xf0,0x00,0xf8,0x00,0xf8,0x00,0x7c,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x00,0xc0,0x00,0xe0,0x00,0xf0,0x00,0xf0,0x00,0xf0,0x00,0xf0,0x00,0xf8,0x00,0xfc,0x00,0xf4,0x00,0xf0,0x00,0x78,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x00,0x03,0x00,0x03,0x00,0x03,0x00,0x07,0x00,0x07,0x00,0x03,0x00,0x01,0x00,0x01,0x00,0x01,0xfc,0xff,0xfe,0xff,0xfe,0xff,0x7e,0xff,0x7e,0xff,0xfe,0xff,0x7e,0xff,0xfe,0xff,0xfe,0xef,0xf0,0x6f,0x3a,0xdf,0xee,0xff,0x1e,0xff,0x1e,0xff,0x1e,0xff,0x96,0xff,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xf9,0x00,0xf8,0x00,0xf8,0x00,0xf8,0x00,0xfc,0x00,0xfc,0x00,0xfc,0x00,0xfe,0x00,0xfe,0x00,0xfe,0x00,0xfe,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x00,0x80,0x00,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x40,0xc0,0x20,0xe0,0x98,0xf8,0xc4,0xfc,0xf2,0xfe,0xfa,0xfe,0xfd,0xff,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xc3,0x00,0xc3,0x00,0xe3,0x00,0xf7,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0x7f,0x00,0x3f,0x00,0x7f,0x00,0x7f,0x00,0x7f,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0x7f,0x00,0x7f,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xbf,0x00,0x1f,0x00,0x1f,0x00,0x07,0x00,0x03,0x00,0x03,0x00,0x03,0x00,0x01,0x00,0x01,0x00,0x81,0x00,0x1d,0x00,0x3f,0x00,0x7f,0x00,0x7f,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xfe,0x00,0xfe,0x00,0xfc,0x00,0xfc,0x00,0xfe,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x02,0x03,0x04,0x07,0x0c,0x0f,0x09,0x0f,0x11,0x1f,0x13,0x1f,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x02,0x03,0x05,0x07,0xe0,0xec,0xf0,0xfe,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xfb,0x00,0xf9,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xbf,0x00,0xdf,0x00,0xeb,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x27,0x3f,0x27,0x3f,0x2f,0x3f,0x2f,0x3f,0x2f,0x3f,0x2f,0x3f,0x3f,0x3f,0x3f,0x3f,0x7f,0x7d,0x21,0x3e,0x36,0x3f,0x5d,0x7f,0x9e,0xff,0x3e,0xff,0x3e,0xff,0x7b,0xff,0x80,0x80,0xc0,0xc0,0xc0,0xc0,0xe0,0xe0,0x80,0x80,0x40,0xc0,0x60,0xe0,0x20,0xe0,0xa0,0xe0,0x90,0xf0,0xc8,0xf8,0xe7,0xff,0xb4,0xfc,0xf4,0xfc,0xf4,0xfc,0x74,0xfc,0x00,0xbe,0x00,0xfe,0x00,0x7f,0x00,0x3f,0x00,0x1f,0x00,0x0f,0x00,0x0f,0x00,0x07,0x00,0x03,0x00,0x03,0x00,0x03,0x00,0x01,0x00,0x01,0x00,0x00,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x00,0x00,0x00,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x02,0x7f,0x7f,0x80,0xff,0x3f,0xff,0x1f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x3f,0xff,0x9e,0xff,0xc0,0xff,0x21,0xff,0x3e,0xff,0x3f,0xff,0x3f,0xff,0x7f,0xff,0xff,0xff,0xff,0xff,0x3f,0xff,0xab,0xff,0xd5,0xff,0xf0,0xff,0xf8,0xff,0x7c,0xff,0xff,0xff,0xff,0xff,0xfd,0xff,0xf8,0xff,0xe0,0xff,0xc0,0xff,0xb3,0xff,0xc0,0xff,0xf1,0xff,0xf0,0xff,0x71,0xbe,0xc4,0xff,0x16,0xff,0x16,0xff,0x00,0xff,0x5e,0xff,0x1f,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xbf,0x00,0x7f,0x00,0x3f,0x00,0x1f,0x00,0x07,0x00,0x07,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01};
     * const unsigned char foreground_asset_pattern_block_2_data [] = {0x06,0xff,0xcf,0x3f,0xcf,0x3f,0x9f,0x7f,0xbf,0x7f,0x7e,0xff,0xfd,0xff,0xfd,0xff,0xff,0xff,0xf7,0xff,0xfe,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xdf,0xff,0xdf,0xff,0xbc,0x7f,0xbc,0x7f,0x3e,0xff,0xfe,0xff,0x5f,0xff,0xdf,0x7f,0x5f,0xff,0xde,0xff,0xbf,0xff,0x3f,0xff,0x7f,0xff,0xff,0xff,0xfe,0xff,0xfe,0xff,0xfd,0xff,0xfd,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x00,0xc0,0x00,0xe0,0x00,0xf0,0x80,0xf8,0x00,0xf0,0x00,0xf0,0x00,0xf0,0x20,0xf0,0x60,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0xf0,0x30,0xf0,0xd0,0xf0,0xe0,0xf0,0xe0,0xf0,0xe0,0xf0,0xff,0xff,0xff,0x7f,0xff,0x7f,0xf1,0x7e,0xfb,0x7e,0xff,0x7c,0xff,0x7c,0xff,0x7d,0x7c,0xbf,0x7b,0xbf,0x7f,0xbf,0x7f,0xbf,0x7f,0xbf,0x7f,0xbf,0x3f,0xdf,0x3f,0xdf,0xc0,0xfc,0xf0,0xfc,0xf8,0xfe,0xf8,0xfe,0xf8,0xff,0xf8,0xff,0xfc,0xff,0xfc,0xff,0xfc,0xff,0xfc,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xf6,0xff,0x06,0xff,0x7f,0xff,0x7f,0xff,0xff,0x7f,0xc7,0x3f,0xef,0xbf,0xff,0x9f,0xff,0x5f,0xff,0x5f,0x9f,0xff,0xef,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x7f,0xff,0xfc,0xff,0xc1,0xff,0xdf,0xef,0xdf,0xef,0x9f,0xef,0x9f,0xef,0x9f,0xef,0xdf,0xef,0xdf,0xef,0xcf,0xf7,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xc0,0xff,0xf0,0xff,0xf8,0xff,0xfe,0xff,0xff,0xff,0xff,0xff,0xce,0x3f,0xef,0x1f,0xe7,0x1f,0xe3,0x1f,0xe9,0x1f,0xe8,0x1f,0xde,0x3f,0xde,0x3f,0x9f,0x7f,0x9f,0x7f,0x9f,0x7f,0xbf,0x7f,0xb7,0x7f,0xbf,0x7f,0xbb,0x7f,0xb9,0x7f,0x00,0x80,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0x80,0xc0,0xf8,0xff,0xf9,0xfe,0xfd,0xfe,0xfc,0xff,0xfe,0xff,0xff,0xff,0xff,0xff,0xdf,0xff,0x3f,0x3f,0x7f,0x7f,0xff,0xff,0x89,0xff,0x00,0xff,0xf0,0xff,0xf8,0xff,0xdc,0x3f,0xf0,0xf8,0xf0,0xf8,0xf0,0xf8,0xf0,0xf8,0xf0,0xf8,0xf0,0xf8,0xf8,0xfc,0xf8,0xfc,0xf8,0xfc,0xf8,0xfc,0xf8,0xfc,0xf8,0xfc,0xf8,0xfc,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0x00,0x00,0x01,0x01,0x0d,0x0d,0x07,0x07,0x05,0x07,0x02,0x03,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x0f,0x0e,0x0f,0x0e,0x0f,0x0e,0x0f,0x0c,0x0f,0x0c,0x0f,0x0c,0x0f,0x0c,0x0f,0x0c,0x0f,0x0c,0x0f,0x0c,0x4f,0x4c,0xef,0xac,0xef,0xec,0xef,0xec,0xef,0xac,0xef,0xac,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,0x02,0x03,0x03,0x03,0x02,0x03,0x03,0x03,0x06,0x07,0x07,0x07,0x3e,0xff,0xc1,0x3e,0xff,0x00,0xf7,0x5d,0xff,0x3e,0x3e,0xdd,0xdd,0xe3,0xe3,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x3f,0xff,0x0f,0xff,0x0f,0xff,0x4f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0x7f,0xdf,0xff,0xff,0xff,0xff,0x7f,0xff,0xff,0xff,0xff,0xff,0xff,0x7f,0xff,0xc9,0xfe,0x38,0xff,0x18,0xff,0xf0,0xff,0x10,0xff,0x20,0xff,0x20,0xff,0xc0,0xff,0xc0,0xff,0xc0,0xff,0x80,0xff,0x80,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x06,0x07,0x05,0x07,0x06,0x07,0x05,0x07,0x07,0x07,0x02,0x03,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x03,0x03,0x07,0x07,0x07,0x07,0x0f,0x0f,0xef,0xf7,0xef,0xf7,0x6f,0xf7,0x67,0xfb,0xf7,0xfb,0x97,0xfb,0x93,0xfd,0xfb,0xfd,0x1b,0xfd,0x3b,0xfd,0xeb,0xfd,0x0b,0xfd,0xfb,0xfd,0x8b,0xfd,0x81,0xfe,0x09,0xfe,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x03,0x03,0x07,0x07,0x06,0x07,0x0d,0x0f,0x1b,0x1f,0x17,0x1f,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xef,0xac,0xbf,0xfe,0xff,0xa1,0xff,0xa1,0xd5,0xaa,0x95,0xea,0x80,0xff,0xc0,0xff,0x60,0x7f,0x33,0x3f,0x18,0x1f,0x0c,0x0f,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0xc0,0x00,0xc0,0x00,0xe0,0x00,0xe0,0x00,0xe0,0x00,0xe0,0x00,0xf0,0x00,0xf0,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x0f,0x0f,0x0f,0x0f,0x0f,0x0f,0x1f,0x1f,0x1f,0x1f,0x1f,0x1f,0x1f,0x1f,0x3f,0x3f,0x3f,0x3f,0x7f,0x7f,0xfe,0xff,0xfe,0xff,0xf7,0xff,0xf4,0xff,0xf8,0xff,0xf4,0xff,0xf6,0xff,0xf1,0xff,0xe0,0xff,0xef,0xff,0xe3,0xff,0xe7,0xff,0xe3,0xff,0xe3,0xff,0xe2,0xff,0xe2,0xff,0x3f,0x3f,0x3f,0x3f,0x7f,0x7f,0x5f,0x7f,0xbf,0xff,0xff,0xff,0xbf,0xff,0x7f,0xff,0xfb,0xfc,0x7f,0xff,0xff,0xff,0x7f,0xd8,0xff,0xfe,0xff,0xff,0xff,0xff,0xf8,0xff,0xdf,0xff,0x9f,0xff,0x9f,0xff,0x3f,0xff,0x3f,0xff,0x7f,0xff,0x7f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80};
     * const unsigned char foreground_asset_pattern_block_3_data [] = {0x38,0xff,0x28,0xff,0x28,0xff,0x28,0xff,0x38,0xef,0x28,0xff,0x38,0xef,0x28,0xff,0x38,0xff,0x38,0xff,0x2b,0xff,0x38,0xff,0x38,0xef,0x28,0xff,0x38,0xef,0x28,0xff,0x6f,0xb0,0xef,0x70,0xf7,0xf8,0x17,0x18,0x1b,0x1c,0x3c,0x3f,0x3f,0x3f,0x7f,0x7f,0x7f,0x5f,0xff,0xdf,0xdf,0xff,0xdc,0xff,0x6b,0xfc,0xbf,0xf8,0x5f,0xfb,0x3d,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xfe,0xfe,0xff,0x07,0xff,0x00,0xff,0x00,0xff,0x00,0xfe,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x03,0xfc,0x07,0xf8,0x01,0xfe,0x80,0xff,0x80,0xff,0x82,0xff,0x03,0xff,0x04,0xff,0x0c,0xff,0x0f,0xff,0xd8,0xff,0xd7,0xff,0xfb,0xff,0xe7,0xf9,0x2b,0xf7,0x2c,0xf3,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0xf1,0xff,0x13,0xff,0x0a,0xfe,0x0e,0xfe,0x02,0xfe,0x1e,0xfe,0x3e,0xfe,0xfe,0xfe,0xec,0xfc,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x40,0xc0,0xe0,0xe0,0x20,0xe0,0x90,0xf0,0x08,0xf8,0x02,0x03,0x02,0x03,0x01,0x01,0x01,0x01,0x00,0x00,0x0f,0x0f,0x04,0x07,0x02,0x03,0x02,0x03,0x01,0x01,0x00,0x00,0x03,0x03,0x03,0x02,0x05,0x07,0x04,0x07,0x04,0x07,0x02,0xfe,0x06,0xfe,0xe1,0x1f,0x38,0xc7,0x00,0xff,0x00,0xff,0x01,0xff,0xc5,0xff,0x27,0xff,0xd4,0xfc,0x0f,0xff,0xcf,0xfc,0xae,0xff,0x88,0xff,0xc8,0xbf,0x63,0x9c,0x38,0xef,0x38,0xef,0x38,0xef,0x28,0xff,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xa0,0xff,0x2d,0xff,0x2d,0xff,0x3d,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x5a,0xff,0x38,0xff,0x2e,0xfd,0x17,0xfe,0x0b,0xff,0x05,0xff,0x02,0xff,0x00,0xff,0x00,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x07,0xff,0x03,0xff,0x01,0xff,0x01,0xff,0xc0,0xff,0xfc,0xff,0xff,0xff,0xfe,0xff,0x79,0xff,0x49,0xff,0x09,0xff,0x89,0xff,0xbf,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xe0,0xe0,0xf8,0x38,0xff,0x0f,0xff,0x01,0x03,0xff,0x36,0xff,0x16,0xff,0x1a,0xff,0x0a,0xff,0x0a,0xff,0x0f,0xff,0x0f,0xff,0x1e,0xff,0x1e,0xff,0xfc,0xff,0xf8,0xff,0x18,0xff,0x18,0xff,0x0c,0xff,0x04,0xff,0x07,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x03,0x03,0x02,0x03,0xef,0x1b,0xec,0x1c,0xe8,0x18,0xd0,0x30,0xb0,0x70,0x78,0xf8,0xf8,0xe8,0xf8,0xe8,0xf8,0xe8,0xfc,0xec,0xef,0xff,0x6d,0xff,0xfa,0x7f,0xfd,0x3f,0xfa,0x9f,0x75,0xff,0xba,0xff,0x34,0xdf,0x68,0xbf,0xf0,0x7f,0xe0,0xff,0x40,0xff,0x41,0xff,0xc3,0x7f,0xc3,0xff,0x87,0xff,0x87,0xff,0xcf,0xff,0x4f,0xff,0x4f,0xff,0x66,0xff,0x26,0xff,0x40,0x7f,0xc0,0xff,0x80,0xff,0x80,0xff,0x86,0xff,0x86,0xff,0x87,0xff,0x8f,0xff,0x8f,0xff,0x8f,0xff,0x8f,0xff,0x87,0xff,0x87,0xff,0x87,0xff,0x83,0xff,0x83,0xff,0x07,0x04,0x03,0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x07,0x07,0x0d,0x0f,0x18,0x1f,0x20,0x3f,0x00,0x3f,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x02,0x02,0x06,0x06,0x0e,0x0e,0x1b,0x1f,0x13,0x1c,0x13,0x1c,0x13,0x1c,0xf3,0xfc,0x00,0x00,0x00,0x00,0xe0,0xe0,0x40,0xc0,0xc0,0xc0,0x80,0x80,0x80,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0xfc,0x03,0xff,0x00,0xff,0x00,0x3f,0xc0,0x00,0xff,0x00,0xff,0x80,0xff,0xff,0xff,0x00,0xff,0xff,0x83,0x7c,0x83,0xbb,0xc7,0xff,0xc7,0xbb,0xc5,0xff,0x83,0x7c,0x83,0x83,0xff,0x81,0xff,0x81,0xff,0x81,0xff,0x80,0xff,0x83,0xff,0x87,0xff,0x8d,0xff,0x80,0xff,0x80,0xff,0x80,0xff,0x80,0xff,0x80,0xff,0x80,0xff,0x80,0xff,0x84,0xff,0xc0,0xff,0xc0,0xff,0x80,0xff,0x80,0xff,0xc0,0xff,0x67,0xff,0x7f,0xff,0x3f,0xff,0x9f,0xff,0xfc,0xff,0x6f,0xff,0x0d,0xff,0x69,0xff,0xc9,0xff,0x7f,0xff,0xff,0xff,0x08,0xf8,0x0c,0xfc,0x04,0xfc,0x04,0xfc,0x04,0xfc,0x06,0xfe,0x06,0xfe,0x06,0xfe,0x02,0xfe,0x02,0xfe,0x02,0xfe,0x02,0xfe,0x03,0xff,0x01,0xff,0x01,0xff,0x01,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0x80,0xff,0x80,0xff,0x80,0xff,0xc0,0xff,0xf0,0xff,0xf8,0xff,0x7d,0xff,0x7f,0xff,0x78,0xff,0x30,0xff,0x30,0xff,0x20,0xff,0x60,0xff,0xfc,0xfc,0xf8,0xf8,0xf0,0xf0,0x50,0xf0,0xf8,0xf8,0x98,0xf8,0xd8,0xf8,0x18,0xf8,0x78,0xf8,0x70,0xf0,0x60,0xe0,0xe0,0xe0,0xe0,0xe0,0xe0,0xe0,0xe0,0xe0,0xe0,0xe0,0xe7,0x18,0xe7,0x18,0xe7,0x18,0xff,0x40,0xff,0x3c,0xff,0x40,0x7e,0x81,0xc3,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x7c,0xff,0xff,0x7c,0xef,0x7c,0xff,0xff,0xef,0xff,0x9e,0xff,0x6f,0x7f,0x1f,0x1f,0x1b,0x1f,0x1d,0x1f,0x3f,0x3f,0x3f,0x3f,0x3e,0x3f,0x38,0x3f,0x1d,0x1f,0x1e,0x1f,0x0e,0x0f,0x0f,0x0f,0x0f,0x0f,0x0f,0x0f,0x0f,0x0f,0xee,0xff,0x6c,0xff,0x6c,0xff,0xef,0x7c,0xef,0xff,0x7d,0xff,0xbb,0xff,0xef,0xff,0xef,0xfb,0xfe,0xbb,0xee,0xbb,0xee,0xbb,0xfe,0xbb,0x82,0xff,0xfe,0x83,0xfc,0xff};
     * const unsigned char * foreground_asset_pattern_block_index [] = {foreground_asset_pattern_block_0_data,foreground_asset_pattern_block_1_data,foreground_asset_pattern_block_2_data,foreground_asset_pattern_block_3_data};
     * const unsigned short foreground_asset_pattern_block_sizes [] = {116,68,64,60};
     * unsigned char portrait_asset_maara_pattern_reference [] = {0x10,0x2e,0x14,0x4,0x10,0x10,0x22,0x3a,0x26,0xa,0x34,0x10,0x10,0x2a,0x1a,0x24,0x0,0x6,0x10,0x10,0x20,0x16,0x12,0x3c,0x1c,0x10,0x1e,0x32,0x2,0x2c,0xe,0x30,0x3e,0x10,0x36,0x38,0x28,0x8,0xc,0x18};
     * struct ForegroundAsset portrait_asset_maara = {2, portrait_asset_maara_pattern_reference};
     *
     *
     * need to move to:
     *
     * // Bank X asset file
     * #pragma bank n
     * const unsigned char foreground_asset_set_SETNAME_pattern_block [] = {0x7d,0xff,0xbd,0xff,0xfd,0xff,0xfd,0xff,0xfd,0xff,0xf9,0xff,0xf9,0xff,0xf9,0xff,0xfb,0xff,0x3b,0xff,0x0b,0x3f,0x03,0x06,0x03,0x07,0x03,0x07,0x03,0x07,0x03,0x07,0xe7,0x18,0xff,0x00,0xdf,0x20,0xfb,0x1c,0xdf,0x20,0xff,0x00,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0xbb,0xff,0xc7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x7f,0xff,0xff,0xff,0x7f,0x7f,0xff,0xbf,0xff,0xdf,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x02,0x03,0x01,0x01,0x02,0x03,0x01,0x01,0x02,0x03,0x01,0x01,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0xc0,0xc0,0xc0,0xc0,0x27,0xe7,0xef,0x3f,0xff,0x01,0xe3,0x1f,0xaf,0x7f,0x7e,0xff,0xbf,0xdf,0xde,0xaf,0x8f,0xff,0xff,0xff,0xff,0xff,0x01,0xff,0x01,0xff,0x55,0xab,0xab,0x55,0xff,0x01,0xff,0x1f,0xff,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0xc0,0xc0,0xe0,0xe0,0xf8,0xf8,0xfc,0xfc,0xff,0xff,0xff,0xff,0xfe,0xff,0xfd,0xfe,0xe0,0xff,0x1f,0xe0,0xf8,0x07,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xff,0x9f,0xff,0xbf,0xff,0xff,0xde,0xff,0xbe,0xcf,0x9e,0xe7,0x0f,0xf7,0x0e,0xf7,0x36,0xcf,0x7c,0xbf,0x7c,0xff,0x7e,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xff,0xff,0xe7,0x18,0x7e,0x81,0xff,0xff,0xd5,0xff,0xeb,0x56,0xc3,0x3c,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0xf8,0xfe,0xfc,0xfe,0x7e,0x7e,0xfe,0xfe,0xfe,0x01,0x54,0xab,0x6b,0xd5,0x94,0xeb,0xeb,0xd5,0x94,0xeb,0xeb,0xff,0xd7,0xff,0xfe,0xfe,0x06,0xfe,0x0e,0xf6,0xfc,0x04,0xff,0xfd,0xff,0xff,0xf3,0xff,0xe1,0xff,0xf9,0xe7,0xf5,0xcb,0xe9,0xd7,0xc1,0xbf,0xe9,0xd7,0xf1,0xef,0xfd,0xf3,0xfd,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbb,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0xff,0xff,0xff,0xaf,0xdf,0xef,0xff,0xdf,0xfe,0xaf,0xdf,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0xff,0x00,0xc3,0x3c,0xff,0x7e,0xff,0x7e,0xc3,0x7e,0xc3,0x3c,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0x70,0xff,0xe3,0xff,0xc3,0xff,0x99,0xe7,0xdf,0xe3,0x99,0xe7,0xfb,0xc7,0xff,0x00,0xff,0x00,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0xf7,0xff,0xe7,0xff,0x00,0xff,0x99,0xe7,0xdf,0xe3,0x99,0xe7,0xfb,0xc7,0xff,0x00,0xff,0x00,0xff,0x00,0xc3,0x3c,0xe7,0x18,0xbd,0xff,0xff,0x00,0xe7,0x18,0xbf,0xc1,0xe7,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfe,0xff,0x7d,0xff,0xaa,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x1f,0x1f,0x0d,0x0e,0x07,0x07,0x03,0x03,0x01,0x01,0x00,0x00,0xbe,0xff,0x26,0xff,0x6c,0xff,0xe8,0xff,0xe8,0xff,0xf0,0xff,0xf0,0xff,0xf9,0xff,0xfd,0xff,0xff,0xff,0xe0,0xfe,0x60,0xfe,0x40,0xfc,0x40,0xf8,0x40,0xf0,0x10,0xf0,0xf6,0x0e,0x6c,0x9c,0x68,0x98,0xd0,0x30,0xb0,0x70,0x7c,0xfc,0xde,0xe6,0x8d,0xf3,0xd5,0xeb,0x8d,0xf3,0xcf,0xf0,0xc7,0xf9,0xce,0xf3,0xdf,0xe3,0xbf,0xc7,0xbf,0xcf,0x87,0xff,0x07,0xff,0x87,0xff,0x83,0xff,0x13,0xef,0x21,0xdf,0x10,0xef,0x28,0xd7,0x55,0xaa,0xaa,0x55,0xff,0x00,0xff,0x00,0xff,0x00,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x00,0x00,0x80,0x80,0x00,0x00,0x80,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xf6,0x0e,0xec,0x1c,0xe8,0x18,0xd0,0x30,0xb0,0x70,0x7c,0xfc,0xde,0xe6,0x8d,0xf3,0xd5,0xeb,0x8d,0xf3,0xcf,0xf0,0xc7,0xf9,0xce,0xf3,0xdf,0xe3,0xbf,0xc7,0xbf,0xcf,0xff,0x7f,0xbf,0xff,0xfe,0x7f,0xbe,0xff,0xfc,0x7f,0xb9,0xff,0xf9,0x7f,0xbb,0xff,0x79,0xff,0xbc,0xff,0x7e,0xff,0xbf,0xff,0x7f,0xff,0xbf,0xff,0x7f,0xff,0xbf,0xff,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x03,0x03,0x0f,0x0f,0x1f,0x1f,0x3f,0x3f,0x07,0x07,0x03,0x03,0x07,0x07,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xbf,0x87,0xff,0xc7,0xff,0x87,0xff,0xc7,0xff,0x07,0xff,0x7e,0xff,0xbf,0xdf,0xdf,0xaf,0x8f,0xff,0xff,0xff,0xff,0xff,0x00,0xff,0x00,0xff,0x55,0xaa,0xaa,0x55,0xff,0x01,0xff,0x1f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xc0,0xc0,0x80,0x80,0xc0,0xc0,0x80,0x80,0x00,0x00,0x00,0x00,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0xff,0xff,0xff,0x3f,0xdf,0xef,0xff,0xdf,0xfe,0xaf,0xdf,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0x70,0xff,0xe3,0xff,0xe7,0xff,0x99,0xe7,0xde,0xe3,0xd8,0xe7,0xfb,0xc7,0xff,0x00,0xe7,0x18,0xfc,0xfc,0xf8,0xfc,0xe8,0xfc,0xe8,0xfc,0xe8,0xfc,0xf0,0xfc,0xf0,0xfc,0xf8,0xfc,0xf8,0xfc,0xfe,0xfe,0xfc,0xfe,0xfc,0xfe,0xf8,0xfe,0xf8,0xfe,0xf8,0xfe,0xf8,0xfe,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x0f,0x0f,0xff,0xff,0xf7,0x0f,0x31,0xcf,0xfc,0xc3,0xe3,0xfc,0x7f,0x7c,0xff,0x7f,0xbf,0xff,0xff,0x7f,0xbf,0xff,0xff,0x7f,0xbf,0xff,0xff,0x7f,0xbf,0xff,0x7f,0xff,0xbf,0xff,0x7f,0xff,0xbe,0xff,0x7e,0xff,0xbe,0xff,0x7e,0xff,0xbe,0xff,0xef,0xb0,0xf6,0xf9,0x16,0x19,0x0b,0x0c,0x1d,0x1e,0x36,0x3f,0x7f,0x6f,0x73,0x4f,0xf5,0x8f,0xdb,0xa7,0x83,0x7f,0xf5,0x0b,0xa9,0xd7,0xf1,0xef,0xf9,0xf7,0xfd,0xfb,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x3f,0xff,0xff,0xff,0xbf,0xdf,0xef,0xff,0xdf,0xfe,0xaf,0xdf,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,0x00,0x00,0x03,0x03,0x0f,0x0f,0x1f,0x1f,0x15,0x1f,0x0a,0x0f,0x0f,0x0f,0x0f,0x0f,0x07,0x07,0x03,0x07,0x01,0x03,0x01,0x01,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xf8,0xf8,0xf8,0xf8,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xf7,0xff,0xf1,0xff,0x77,0xff,0xbb,0xff,0xf5,0xfb,0x7b,0xff,0xaf,0xf0,0xef,0x70,0x7f,0xff,0x08,0xff,0xb3,0xcf,0x5f,0xf5,0x3f,0xf9,0x13,0xfd,0x12,0xff,0x13,0xff,0xff,0x7f,0xb5,0xdf,0xb7,0xdf,0xbf,0xc7,0x9f,0xe7,0x8f,0xf7,0x87,0xff,0x87,0xff,0x00,0xe0,0x60,0xe0,0xe0,0xe0,0xc0,0xc0,0xc0,0xc0,0xc0,0xc0,0x40,0xc0,0x40,0xc0,0x40,0xc0,0xc0,0x40,0xc0,0xc0,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0x80,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xf7,0xff,0xf9,0xff,0x77,0xff,0xb9,0xff,0xf5,0xfa,0x7b,0xff,0xaf,0xf0,0xef,0x70,0xef,0xff,0xff,0xe7,0xf7,0xef,0xe7,0xff,0xff,0xff,0xff,0xff,0x00,0xff,0x00,0xff,0x55,0xaa,0xaa,0x55,0xff,0x00,0xff,0x00,0xff,0xf0,0xff,0xff,0xff,0xff,0xff,0xff,0x1f,0x1f,0x0f,0x1f,0x3f,0x3f,0x3b,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x7d,0x7f,0x7d,0x7f,0x7d,0x7f,0x5c,0x7f,0x5c,0x7f,0x5c,0x7f,0x7d,0x7f,0x3c,0x7f,0x03,0x07,0x07,0x07,0x07,0x07,0x05,0x07,0x03,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x02,0x03,0x03,0x03,0x03,0x03,0x01,0x01,0x01,0x01,0x7d,0x7f,0x7d,0x7f,0x7d,0x7f,0xbd,0xff,0xfd,0xff,0xf9,0xff,0xf9,0xff,0xf9,0xff,0xfb,0xff,0xfb,0xff,0xfb,0xff,0xfb,0xfe,0xfb,0xff,0xfb,0xff,0xfb,0xff,0xfb,0xff,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfc,0xfe,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xfe,0xff,0xef,0xb0,0xf7,0xf8,0x17,0x18,0x0b,0x0c,0x1d,0x1e,0x36,0x3f,0x7f,0x6f,0x73,0x4f,0xf5,0x8f,0xdb,0xa7,0x83,0x7f,0xf5,0x0b,0xa9,0xd7,0xf1,0xef,0xf9,0xf7,0xfd,0xfb,0x3f,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0x78,0xff,0x70,0xff,0xc3,0xff,0xe7,0xff,0x81,0xff,0xdf,0xe3,0x99,0xe7,0xfb,0xc7,0xe7,0x18,0xe7,0x18,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xbf,0xff,0xff,0xff,0xbf,0xdf,0xef,0xff,0xff,0xfe,0xef,0x7f,0xde,0xef,0xf4,0x0f,0xf5,0x0f,0x80,0x80,0x00,0x00,0x00,0x00,0x80,0x80,0x80,0x80,0xe0,0xe0,0xf0,0xf0,0xe0,0xe0,0x80,0x80,0x80,0x80,0xc0,0xc0,0xe0,0xe0,0x80,0x80,0x80,0x80,0x80,0x80,0x00,0x00,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfd,0xff,0xf7,0xff,0xf8,0xff,0x77,0xff,0xbb,0xff,0xf5,0xfb,0x7b,0xff,0xaf,0xf0,0xef,0x70,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x80,0x80,0x80,0x80,0x00,0x80,0xc0,0xc0,0xf0,0xf0,0xf8,0xf8,0xf8,0xf8,0x1f,0x1f,0x0f,0x1f,0x3f,0x3f,0x3b,0x3f,0x3f,0x3f,0xff,0xff,0xff,0x7f,0xff,0x3f,0x3d,0xff,0x3d,0xff,0xbd,0xff,0x9c,0xff,0x9c,0x7f,0xdc,0xff,0x1d,0xff,0xbc,0xff,0xf3,0xff,0xff,0xff,0xff,0xff,0xfd,0xff,0xff,0x03,0x56,0xab,0xaa,0x5f,0x56,0xaf,0xae,0x5f,0x56,0xff,0xae,0xff,0xfe,0xff,0xc3,0xff,0x83,0xff,0xfd,0x83,0x7f,0x41,0xfc,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xff,0xfc,0xff,0xf7,0xff,0xf9,0xff,0x77,0xff,0xbb,0xff,0xf5,0xfb,0x7b,0xff,0xaf,0xf0,0xef,0x70,0x00,0x00,0x00,0x00,0x00,0x00,0x06,0x06,0x0f,0x09,0x0f,0x08,0x1d,0x12,0x3d,0x32,0x7d,0x72,0x6f,0x52,0x4f,0x72,0x4f,0x70,0x4f,0x7e,0x31,0x2f,0x30,0x2f,0x1a,0x1f};
     * unsigned char foreground_asset_set_SETNAME_portrait_asset_raye_pattern_reference [] = {0x6,0x26,0x3a,0x1a,0x6,0x6,0x2a,0x32,0x3e,0x12,0x18,0x6,0x30,0x2,0x4,0x3c,0x8,0x34,0x0,0x10,0x24,0x22,0x40,0x2c,0xc,0x6,0x14,0x42,0x1e,0x2e,0x16,0x36,0xe,0x6,0x1c,0x20,0x28,0x28,0x38,0xa};
     * struct ForegroundAsset portrait_asset_raye {1 /bank number/, foreground_asset_set_SETNAME_pattern_block, foreground_asset_set_SETNAME_portrait_asset_raye_pattern_reference};
     *
     * In main C script:
     *
     * extern struct ForegroundAsset portrait_asset_raye;
     */

    private final String assetName;
    @Getter private final List<ForegroundAsset> foregroundAssets;
    private final PatternBlock patternBlock;

    @Override
    public long getSize() {
        return patternBlock.sizeInBytes() +
                this.foregroundAssets.stream()
                        .map(ForegroundAsset::sizeInBytes)
                        .reduce(Integer::sum)
                        .orElse(0);
    }

    @Override
    public String getId() {
        return this.assetName;
    }
}
