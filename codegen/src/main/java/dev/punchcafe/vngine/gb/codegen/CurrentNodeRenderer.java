package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.csan.NodeIdSanitiser;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.CURRENT_NODE_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.NODE_DEFINITION_RENDERER_NAME;

@Builder
public class CurrentNodeRenderer implements ComponentRenderer {

    private ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        return Optional.ofNullable(gameConfig.getChapterConfigs().get(0).getFirstNodeId())
                .or(() -> Optional.ofNullable(gameConfig.getChapterConfigs().get(0).getNodes().get(0).getId()))
                .map(NodeIdSanitiser::sanitiseNodeId)
                .map(id -> String.format("struct Node * current_node = &%s;", id))
                .get();
    }

    @Override
    public List<String> dependencies() {
        return List.of(NODE_DEFINITION_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return CURRENT_NODE_RENDERER_NAME;
    }
}
