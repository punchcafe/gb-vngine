package dev.punchcafe.vngine.gb.codegen.render.narrative;

import dev.punchcafe.vngine.gb.codegen.narrative.SimpleNarrative;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.NARRATIVE_DEFINITION_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.NARRATIVE_RENDERER_NAME;

@Builder
public class NarrativeRenderer implements ComponentRenderer {

    private final ProjectObjectModel<SimpleNarrative> gameConfig;

    @Override
    public String render() {
        return gameConfig.getNarrativeConfigs().stream()
                .map(this::renderSingleNarrative)
                .collect(Collectors.joining("\n"));
    }

    private String renderSingleNarrative(final SimpleNarrative narrative){
        return String.format("struct Narrative %s = {\"%s\"};", narrative.getId(), narrative.getMessages());
    }

    @Override
    public List<String> dependencies() {
        return List.of(NARRATIVE_DEFINITION_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return NARRATIVE_RENDERER_NAME;
    }
}
