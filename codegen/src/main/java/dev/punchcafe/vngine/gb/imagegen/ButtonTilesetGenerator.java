package dev.punchcafe.vngine.gb.imagegen;

import dev.punchcafe.vngine.gb.codegen.narrative.config.FontConfig;
import dev.punchcafe.vngine.gb.codegen.narrative.config.TextTheme;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ButtonTilesetGenerator implements ComponentRenderer {

    private static final String DARK_BUTTON_SET_PATH = "src/main/resources/assets/button-tileset/dark_tileset.btn.asset.png";
    private final FontConfig fontConfig;
    private final HexValueConfig hexValueConfig;
    private final Map<TextTheme, String> themeToTilesetPath = Map.of(TextTheme.dark, DARK_BUTTON_SET_PATH);

    public static ButtonTilesetGenerator fromConfig(final FontConfig config){
        return new ButtonTilesetGenerator(config);
    }

    private ButtonTilesetGenerator(final FontConfig fontConfig) {
        this.fontConfig = fontConfig;
        this.hexValueConfig = new HexValueConfig();
    }

    @Override
    public String render() {
        return Stream.of(this.fontConfig.getTheme())
                .map(themeToTilesetPath::get)
                .map(File::new)
                .map(this::openImage)
                .map(image -> TileConverters.extractTilesFromImage(image, this.hexValueConfig))
                .flatMap(List::stream)
                .map(Tile::toGBDKCode)
                .collect(CArrayCollector.forArrayName("button_tile_set_data"));

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
        return null;
    }
}
