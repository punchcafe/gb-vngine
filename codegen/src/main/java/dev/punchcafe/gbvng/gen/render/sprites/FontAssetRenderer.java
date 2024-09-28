package dev.punchcafe.gbvng.gen.render.sprites;

import dev.punchcafe.gbvng.gen.adapter.graphics.Tile;
import dev.punchcafe.gbvng.gen.project.assets.AssetFile;
import dev.punchcafe.gbvng.gen.project.assets.AssetsIndex;
import dev.punchcafe.gbvng.gen.project.config.FontConfig;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static dev.punchcafe.gbvng.gen.adapter.graphics.TileConverters.extractTilesFromImage;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.FONT_ASSET_RENDERER_NAME;
import static java.util.stream.Collectors.joining;

public class FontAssetRenderer implements ComponentRenderer {

    private final FontConfig config;
    private final HexValueConfig hexValueConfig;
    private final AssetFile fontAssetFile;

    public FontAssetRenderer(final AssetsIndex assetsIndex,
                             final FontConfig fontConfig,
                             final HexValueConfig hexValueConfig){
        this.config = fontConfig;
        this.hexValueConfig = hexValueConfig;
        final var fontAssets = assetsIndex.allFontAssetFiles();
        if(fontAssets.size() != 1){
            throw new RuntimeException("Too many font assets found, only one font asset in the asset folder allowed.");
        }
        this.fontAssetFile = fontAssets.get(0);
    }

    @Override
    public String render() {
        final var image = openImage(this.fontAssetFile.getFile());
        final var tileData = extractTilesFromImage(image, config.getSanitisedCharacterSet().length, hexValueConfig)
                .stream()
                .map(Tile::toGBDKCode)
                .collect(joining(",\n", "\n", "\n"));
        return String.format("const unsigned char %s [] = {%s};", this.fontAssetFile.getAssetName(), tileData);
    }

    private BufferedImage openImage(final File file){
        try{
            return ImageIO.read(file);
        } catch (IOException ex){
            throw new RuntimeException();
        }
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return FONT_ASSET_RENDERER_NAME;
    }
}
