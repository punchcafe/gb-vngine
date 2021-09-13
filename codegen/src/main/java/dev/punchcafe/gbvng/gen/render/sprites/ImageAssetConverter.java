package dev.punchcafe.gbvng.gen.render.sprites;

import java.awt.image.BufferedImage;

public interface ImageAssetConverter {

    String convert(final BufferedImage image, final String assetName);

    String supportedAssetExtension();
}
