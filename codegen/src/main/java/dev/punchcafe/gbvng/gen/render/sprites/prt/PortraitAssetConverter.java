package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;

import java.io.File;
import java.util.List;

public class PortraitAssetConverter implements ComponentRenderer {


    private final File assetDirectory;
    private final HexValueConfig hexValueConfig;
    private final List<PortraitAsset> assets;

    private PortraitAssetConverter(final File assetDirectory, final HexValueConfig hexValueConfig){
        final var imageConverter = new PortraitImageConvert(hexValueConfig);
        this.assetDirectory = assetDirectory;
        this.hexValueConfig = hexValueConfig;
        this.assets = imageConverter.extractAllAssetsFromDirectory(assetDirectory);
    }

    @Override
    public String render() {
        return null;
    }

    @Override
    public List<String> dependencies() {
        return null;
    }

    @Override
    public String componentName() {
        return null;
    }
}
