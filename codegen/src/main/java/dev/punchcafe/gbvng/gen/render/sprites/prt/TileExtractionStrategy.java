package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.render.sprites.TallTile;

import java.awt.image.BufferedImage;
import java.util.List;

public interface TileExtractionStrategy {
    List<TallTile> extractTallTilesFromImage(BufferedImage image, HexValueConfig hexValueConfig);
}
