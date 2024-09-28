package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.adapter.graphics.CompositeSprite;
import dev.punchcafe.gbvng.gen.project.assets.AssetFile;
import dev.punchcafe.gbvng.gen.project.assets.AssetType;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.adapter.graphics.TallTile;
import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds and converts all portrait images in file directorys and returns the model {@link CompositeSprite}
 */
@AllArgsConstructor()
public class ForegroundImageConverter {

    private static Map<AssetType, TileExtractionStrategy> TILE_EXTRACTION_STRATEGY_MAP = Map.of(
            AssetType.PORTRAIT_IMAGE, new PortraitTileExtractionStrategy(),
            AssetType.FOCUS_IMAGE, new FocusTileExtractionStrategy());

    private HexValueConfig hexValueConfig;

    public CompositeSprite convertFileToAsset(final AssetFile assetFile) {
        return Optional.of(assetFile.getFile())
                .map(this::openImage)
                .map(image -> this.extractTallTilesFromImage(image, assetFile.getType()))
                .map(tiles -> CompositeSprite.builder().imageData(tiles).name(assetFile.getAssetName()).build())
                .get();
    }

    private BufferedImage openImage(final File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    private List<TallTile> extractTallTilesFromImage(final BufferedImage image, final AssetType imageType) {
        return Optional.of(imageType)
                .map(TILE_EXTRACTION_STRATEGY_MAP::get)
                .map(strategy -> strategy.extractTallTilesFromImage(image, this.hexValueConfig))
                .orElseThrow(() -> new UnsupportedOperationException(String.format("No extraction strategy found for images of type %s", imageType)));
    }
}
