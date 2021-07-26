package dev.punchcafe.vngine.gb.codegen.narrative;

import dev.punchcafe.vngine.pom.NarrativeAdaptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NarrativeReader implements NarrativeAdaptor<SimpleNarrative> {
    @Override
    public List<SimpleNarrative> parseNarrativesFromDirectory(File file) {
        return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .map(this::convertFile)
                .collect(Collectors.toUnmodifiableList());

    }

    private SimpleNarrative convertFile(final File file) {
        try {
            return SimpleNarrative.builder()
                    .id(file.getName())
                    .messages(Files.lines(file.toPath()).collect(Collectors.toUnmodifiableList()))
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}
