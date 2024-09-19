package dev.punchcafe.vngine.pom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.punchcafe.vngine.pom.model.GameStateVariableConfig;
import dev.punchcafe.vngine.pom.model.ChapterConfig;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import dev.punchcafe.vngine.pom.model.Spec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static dev.punchcafe.vngine.pom.GameConfigConstants.*;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PomLoader<N> {

    public static <N> PomLoader forGame(final File rootProjectDirectory, final NarrativeAdaptor<N> narrativeAdaptor) {
        return new PomLoader<>(rootProjectDirectory, narrativeAdaptor);
    }

    private final File rootProjectDirectory;
    private final NarrativeAdaptor<N> narrativeAdaptor;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public ProjectObjectModel loadGameConfiguration() {
        return Optional.of(ProjectObjectModel.emptyProject())
                .map(this::loadGameStateVariables)
                .map(this::loadChapterConfigs)
                .map(this::loadNarrativeConfig)
                .map(this::loadSpec)
                .get();
    }

    private ProjectObjectModel loadGameStateVariables(final ProjectObjectModel existingConfig) {
        final var gameStateVariablesDeclarationFile = findFile(GAME_STATE_VARIABLE_DECLARATION_FILE_NAME,
                this.rootProjectDirectory);
        try {
            final var configObject = mapper.readValue(gameStateVariablesDeclarationFile, GameStateVariableConfig.class);
            return existingConfig.toBuilder()
                    .gameStateVariableConfig(configObject)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidConfigurationFile(GAME_STATE_VARIABLE_DECLARATION_FILE_NAME);
        }
    }

    private ProjectObjectModel loadSpec(final ProjectObjectModel existingConfig) {
        final var specFile = findFile(SPEC_DIRECTORY_NAME,
                this.rootProjectDirectory);
        try {
            final var specObject = mapper.readValue(specFile, Spec.class);
            return existingConfig.toBuilder()
                    .spec(specObject)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidConfigurationFile(SPEC_DIRECTORY_NAME);
        }
    }

    private ProjectObjectModel loadNarrativeConfig(final ProjectObjectModel existingConfig) {
        final var narrativesDirectory = findFile(NARRATIVE_CONFIGS_DIRECTORY_NAME,
                this.rootProjectDirectory);
        return existingConfig.toBuilder()
                .narrativeConfigs(this.narrativeAdaptor.parseNarrativesFromDirectory(narrativesDirectory))
                .build();
    }

    private ProjectObjectModel loadChapterConfigs(final ProjectObjectModel existingConfig) {
        final var chapterConfigsDirectory = findFile(CHAPTER_CONFIGS_DIRECTORY_NAME,
                this.rootProjectDirectory);
        try {
            final var chapterConfigs = new ArrayList<ChapterConfig>();
            for(final var file : chapterConfigsDirectory.listFiles()){
                chapterConfigs.add(mapper.readValue(file, ChapterConfig.class));
            }
            return existingConfig.toBuilder()
                    .chapterConfigs(chapterConfigs)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidConfigurationFile(GAME_STATE_VARIABLE_DECLARATION_FILE_NAME);
        }
    }

    private static File findFile(final String fileName, final File directory) {
        final var matchingFile = directory.listFiles(file -> file.getName().equals(fileName));
        if (matchingFile.length != 1) {
            throw new MissingConfigurationFile(fileName);
        }
        return matchingFile[0];
    }
}
