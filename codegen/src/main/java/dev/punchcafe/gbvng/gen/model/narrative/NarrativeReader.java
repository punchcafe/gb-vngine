package dev.punchcafe.gbvng.gen.model.narrative;

import dev.punchcafe.vngine.pom.NarrativeAdaptor;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NarrativeReader implements NarrativeAdaptor<Narrative> {

    private static String NARRATIVE_FILE_EXTENSION = ".vnx";

    private final Persister serializer = new Persister();

    @Override
    public List<Narrative> parseNarrativesFromDirectory(File file) {
        return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(this::isNarrativeXmlFile)
                .map(this::convertFile)
                .collect(Collectors.toUnmodifiableList());

    }

    private boolean isNarrativeXmlFile(final File file){
        return file.getName().endsWith(NARRATIVE_FILE_EXTENSION);
    }

    private Narrative convertFile(final File file) {
        try {
            return serializer.read(Narrative.class, file);
        } catch (IOException ex) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
