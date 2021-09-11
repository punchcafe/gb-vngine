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

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.BUTTON_TILESET_RENDERER_NAME;

public class ButtonTilesetGenerator implements ComponentRenderer {

    //todo: set character position header macros here

    public static String BUTTON_TILESET_SIZE_MACRO = "BUTTON_SET_SIZE";
    public static int BUTTON_TILESET_SIZE = 7;
    private static String HEADERS = "#define BLANK_SPACE_CHAR_POSITION 0\n" +
            "#define PRESS_A_BUTTON_NON_PRESSED_POSITION 1\n" +
            "#define PRESS_A_BUTTON_PRESSED_POSITION 2\n" +
            "#define PRESS_UP_BUTTON_PRESSED_POSITION 3\n" +
            "#define PRESS_DOWN_BUTTON_PRESSED_POSITION 4\n" +
            "#define PRESS_LEFT_BUTTON_PRESSED_POSITION 5\n" +
            "#define PRESS_RIGHT_BUTTON_PRESSED_POSITION 6\n";

    private static final String DARK_BUTTON_SET_PATH = "src/main/resources/assets/button-tileset/dark_tileset.btn.asset.png";
    private final FontConfig fontConfig;
    private final HexValueConfig hexValueConfig;
    private final Map<TextTheme, String> themeToTilesetPath = Map.of(TextTheme.dark, DARK_BUTTON_SET_PATH);

    public static ButtonTilesetGenerator fromConfig(final FontConfig config,final HexValueConfig hexValueConfig ){
        return new ButtonTilesetGenerator(config, hexValueConfig);
    }

    private ButtonTilesetGenerator(final FontConfig fontConfig, final HexValueConfig hexValueConfig) {
        this.fontConfig = fontConfig;
        this.hexValueConfig = hexValueConfig;
    }

    @Override
    public String render() {
        final var data = Stream.of(this.fontConfig.getTheme())
                .map(themeToTilesetPath::get)
                .map(File::new)
                .map(this::openImage)
                .map(image -> TileConverters.extractTilesFromImage(image, this.hexValueConfig))
                .flatMap(List::stream)
                .map(Tile::toGBDKCode)
                .collect(CArrayCollector.forArrayName("button_tile_set_data"));

        return  HEADERS +
                String.format("#define %s %d\n", BUTTON_TILESET_SIZE_MACRO, BUTTON_TILESET_SIZE) +
                data;
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
        return BUTTON_TILESET_RENDERER_NAME;
    }
}
