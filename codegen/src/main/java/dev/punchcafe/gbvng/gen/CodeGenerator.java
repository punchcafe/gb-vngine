/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dev.punchcafe.gbvng.gen;

import dev.punchcafe.gbvng.gen.mbanks.MemoryBank;
import dev.punchcafe.gbvng.gen.mbanks.renderers.BankRenderer;
import dev.punchcafe.gbvng.gen.mbanks.utility.ForegroundAssetSetExtractor;
import dev.punchcafe.gbvng.gen.model.narrative.Narrative;
import dev.punchcafe.gbvng.gen.model.narrative.NarrativeReader;
import dev.punchcafe.gbvng.gen.config.ColorConfig;
import dev.punchcafe.gbvng.gen.config.ImageConfig;
import dev.punchcafe.gbvng.gen.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.model.narrative.PlayMusic;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.render.sprites.PixelValue;
import dev.punchcafe.vngine.pom.PomLoader;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Getter;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CodeGenerator {

    private static class AppArgs {

        private String outputFile;
        @Getter private String projectRootDirectory;

        public Optional<String> getOutputFile(){
            return Optional.ofNullable(this.outputFile);
        }

        public AppArgs mergeFlags(final Map<String, String> flags){
            this.outputFile = flags.get("o");
            return this;
        }

        public AppArgs mergeRootDirectory(final List<String> orderedArgs){
            this.projectRootDirectory = orderedArgs.get(0);
            return this;
        }
    }

    private static Map<String, String> parseFlags(String[] args){
        final Map<String,String> flags = new HashMap<>();
        int i = 0;
        while(i < args.length){
            if(args[i].startsWith("-")){
                flags.put(args[i].substring(1), args[i+1]);
                i += 2;
            } else {
                i++;
            }
        }
        return flags;
    }

    private static List<String> parseOrderedArgs(String[] args){
        final List<String> orderedArgs = new ArrayList<>();
        int i = 0;
        while(i < args.length){
            if(args[i].startsWith("-")){
                i += 2;
            } else {
                orderedArgs.add(args[i]);
                i++;
            }
        }
        return orderedArgs;
    }

    private static AppArgs convertArgs(String[] args){
        final var argsModel = new AppArgs();
        return argsModel.mergeFlags(parseFlags(args))
                .mergeRootDirectory(parseOrderedArgs(args));
    }

    public static void main(String[] args) throws IOException {
         new CodeGenerator().run(args);
    }

    private final Persister serializer = new Persister();

    public void run(final String[] args) throws IOException {
        final CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.run(CodeGenerator.convertArgs(args));
    }

    public void run(final AppArgs appArgs) throws IOException {
        run(new File(appArgs.projectRootDirectory), appArgs.getOutputFile().orElse("./game.c"));
    }

    public void run(final File vngProjectRoot, final String scriptDestination) throws IOException {
        final var narrativeReader = new NarrativeReader();

        final var assetsDirectory = vngProjectRoot.listFiles((root, fileName) -> fileName.equals("assets"))[0];
        final var narrativeConfig = Optional.ofNullable(assetsDirectory.listFiles((file, name) -> name.equals("config.vnx")))
                .stream()
                .flatMap(Arrays::stream)
                .map(this::parseNarrativeConfigFile)
                .findAny()
                .orElseThrow();


        final HexValueConfig hexConfig = extractHexConfig(narrativeConfig.getImageConfig());

        final var foregroundAssetSets = new ForegroundAssetSetExtractor(assetsDirectory, hexConfig, narrativeConfig)
                .convertAllForegroundAssetSets()
                .stream()
                //TODO: fix this ugly monstrosity
                .map(assetSet -> {
                    assetSet.assignBank(3);
                    return assetSet;
                }).collect(toList());

        final ProjectObjectModel<Narrative> gameConfig = PomLoader.<Narrative>forGame(vngProjectRoot, narrativeReader).loadGameConfiguration();

        final var hasMusic = gameConfig.getNarrativeConfigs().stream()
                .map(Narrative::getElements)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .anyMatch(elem -> elem.getClass().equals(PlayMusic.class));

        final var rendererFactory = new RendererFactory(gameConfig,
                assetsDirectory,
                narrativeConfig,
                hexConfig,
                foregroundAssetSets,
                hasMusic);

        final var allComponents = Arrays.stream(rendererFactory.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RendererSupplier.class))
                .map(supplierMethod -> getFromMethod(supplierMethod, rendererFactory))
                .collect(toList());

        final var scriptRenderer = ScriptRenderer.builder()
                .componentRenderers(allComponents)
                .build();

        final var renderedScript = scriptRenderer.render();
        final var out = new BufferedWriter(new FileWriter(scriptDestination));

        out.write(renderedScript);
        out.close();

        final var bankWriteLocation = Path.of(scriptDestination).getParent();
        final var memoryBankWriter = new BankRenderer(bankWriteLocation.toFile());

        //TODO: replace with clever optimisation
        final var sampleBank = MemoryBank.builder().build();
        for(final var asset : foregroundAssetSets){
            sampleBank.addAsset(asset);
        }
        System.out.println(foregroundAssetSets.size());
        System.out.println(sampleBank.getAssets().size());
        memoryBankWriter.generateBankFile(sampleBank, 3);
    }

    private HexValueConfig extractHexConfig(ImageConfig imageConfig) {
        final var hexMap = imageConfig.getPaletteConfig().getColors().stream()
                .map(this::convertColorToEntry)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return HexValueConfig.builder()
                .hexConversions(hexMap)
                .build();
    }

    private Map.Entry<String, PixelValue> convertColorToEntry(final ColorConfig color){
        // TODO: make this a model which does checks for us.
        return Map.entry(color.getHex(), pixelValueFromString(color.getValue()));
    }

    private PixelValue pixelValueFromString(final String val){
        switch (val){
            case "0":
                return PixelValue.VAL_0;
            case "1":
                return PixelValue.VAL_1;
            case "2":
                return PixelValue.VAL_2;
            case "3":
                return PixelValue.VAL_3;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private NarrativeConfig parseNarrativeConfigFile(final File file){
        try {
            return serializer.read(NarrativeConfig.class, file);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private ComponentRenderer getFromMethod(final Method method, final RendererFactory factory) {
        try {
            return (ComponentRenderer) method.invoke(factory);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
