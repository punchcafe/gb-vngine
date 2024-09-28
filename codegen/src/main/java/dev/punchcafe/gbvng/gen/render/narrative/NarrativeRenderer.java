package dev.punchcafe.gbvng.gen.render.narrative;

import dev.punchcafe.gbvng.gen.adapter.NarrativeName;
import dev.punchcafe.gbvng.gen.adapter.assets.TextAsset;
import dev.punchcafe.gbvng.gen.project.narrative.Narrative;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;
import static java.util.stream.Collectors.joining;

@Builder
public class NarrativeRenderer implements ComponentRenderer {

    private final ProjectObjectModel<Narrative> gameConfig;
    private final Map<String, TextAsset> textAssetCache;

    @Override
    public String render() {
        return gameConfig.getNarrativeConfigs().stream()
                .map(this::renderSingleNarrative)
                .collect(joining("\n"));
    }

    private String renderSingleNarrative(final Narrative narrative){
        if(narrative.getElements() == null){
            return "";
        }
        final int numberOfElements = narrative.getElements().size();
        final var elements = Stream.<String>builder();
        final var elementNames = Stream.<String>builder();
        for(int i = 0; i < numberOfElements; i++){
            final var elementRenderer = new NarrativeElementRenderer(i, narrative.getNarrativeId(), this.textAssetCache);
            elements.add(narrative.getElements().get(i).acceptVisitor(elementRenderer));
            elementNames.add(NarrativeName.elementName(narrative.getNarrativeId(), i));
        }
        final var renderedElements = elements.build().collect(joining("\n"));

        final var narrativeElementsArrayBody = elementNames.build()
                .map(elemName -> String.format("&%s", elemName))
                .collect(joining(",", "{", "}"));
        final var narrativeElementArray = String.format("struct NarrativeElement *%s [%d] = %s;",
                NarrativeName.elementArrayName(narrative.getNarrativeId()),
                numberOfElements,
                narrativeElementsArrayBody);
        final var narrativeStruct = String.format("struct Narrative %s = {%d, %s};",
                NarrativeName.narrativeName(narrative.getNarrativeId()),
                numberOfElements,
                NarrativeName.elementArrayName(narrative.getNarrativeId()));

        return renderedElements + "\n" + narrativeElementArray + "\n" + narrativeStruct;
    }

    @Override
    public List<String> dependencies() {
        return List.of(TEXT_RENDERER_RENDERER_NAME,
                FOREGROUND_ELEM_STRUCT_RENDERER_NAME,
                TEXT_ELEM_STRUCT_RENDERER_NAME,
                PAUSE_ELEM_STRUCT_RENDERER_NAME,
                NARRATIVE_STRUCT_RENDERER_NAME,
                FOREGROUND_RENDERER_RENDERER_NAME,
                EXTERNAL_BACKGROUND_ASSET_RENDERER_NAME,
                FONT_ASSET_RENDERER_NAME,
                TEXT_RENDERER_RENDERER_NAME,
                DELAY_WITH_MUSIC_RENDERER_NAME,
                EXTERNAL_TEXT_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return NARRATIVE_RENDERER_NAME;
    }
}
