package dev.punchcafe.vngine.gb.codegen.render.transition;

import dev.punchcafe.vngine.gb.codegen.csan.NodeTransitionName;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.NodeType;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.*;

@Builder
public class TransitionDeclarer implements ComponentRenderer {

    private final ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        return gameConfig.getChapterConfigs().get(0).getNodes()
                .stream()
                .map(this::renderNodeTransition)
                .collect(Collectors.joining("\n"));
    }

    private String renderNodeTransition(final Node node) {
        final var transitionTypeName = getTransitionTypeName(node.getType());
        final var variableName = NodeTransitionName.getTransitionNameForNode(node);
        return String.format("struct %s %s;", transitionTypeName, variableName);
    }

    private String getTransitionTypeName(final NodeType type) {
        switch (type) {
            case AUTOMATIC:
                return "PredicateBasedTransition";
            case PLAYER:
                return "PlayerBasedTransition";
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<String> dependencies() {
        return List.of(PLAYER_TRANSITION_DEFINITION_RENDERER_NAME,
                PREDICATE_TRANSITION_DEFINITION_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return TRANSITION_INITIALIZER_RENDERER_NAME;
    }
}
