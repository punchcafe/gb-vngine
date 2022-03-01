package dev.punchcafe.gbvng.gen.render.sprites;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.IMAGE_ASSET_RENDERER_NAME;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class ImageAssetsGenerator implements ComponentRenderer {

    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.(fnt|bkg)\\.asset\\..+$");

    private final File assetDirectory;
    private final Map<String, ImageAssetConverter> strategies;

    public ImageAssetsGenerator(final List<ImageAssetConverter> assetConverters, final File assetDirectory){
        this.assetDirectory = assetDirectory;
        this.strategies = assetConverters.stream()
                .collect(toMap(ImageAssetConverter::supportedAssetExtension, identity()));
    }

    @Override
    public String render() {
        return recursiveAssetRender(this.assetDirectory);
    }

    private String recursiveAssetRender(final File file){
        final var assets = Optional.ofNullable(file.listFiles(this::isAsset))
                .stream()
                .flatMap(Arrays::stream)
                .map(this::convertAssetFile)
                .collect(Collectors.joining("\n"));
        final var recursive = Optional.ofNullable(file.listFiles(File::isDirectory))
                .stream()
                .flatMap(Arrays::stream)
                .map(this::recursiveAssetRender)
                .collect(Collectors.joining("\n"));
        return assets.concat(recursive);
    }

    private String convertAssetFile(final File file){
        final var match = IMAGE_ASSET_EXTENSION.matcher(file.getName());
        match.find();
        final var assetName = match.group(1);
        final var assetType = match.group(2);
        return Optional.of(assetType)
                .map(strategies::get)
                .map(strategy -> strategy.convert(openImage(file), assetName))
                .orElseThrow();
    }

    private BufferedImage openImage(final File file){
        try{
            return ImageIO.read(file);
        } catch (IOException ex){
            throw new RuntimeException();
        }
    }

    private boolean isAsset(final File dir, final String fileName){
        return IMAGE_ASSET_EXTENSION.matcher(fileName).matches();
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return IMAGE_ASSET_RENDERER_NAME;
    }
}
