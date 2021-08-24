package dev.punchcafe.vngine.gb.imagegen;

import java.awt.image.BufferedImage;

public interface ImageAssetConverter {

    String convert(final BufferedImage file);

    String supportedAssetExtension();
}
