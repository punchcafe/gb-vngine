package dev.punchcafe.gbvng.gen.render.node;

import dev.punchcafe.gbvng.gen.csan.NodeMutationsArrayName;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.csan.NarrativeName;
import dev.punchcafe.gbvng.gen.csan.NodeIdSanitiser;
import dev.punchcafe.gbvng.gen.csan.NodeTransitionName;
import dev.punchcafe.gbvng.gen.narrative.Narrative;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.NodeType;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;

@Builder
@Getter
public class NodeRenderer implements ComponentRenderer {

    private final ProjectObjectModel<Narrative> gameConfig;

    @Override
    public String render() {
        return gameConfig.getChapterConfigs().get(0).getNodes().stream()
                .map(this::renderNode)
                .collect(Collectors.joining("\n"));
    }

    private String renderNode(final Node node) {
        final String nodeId = NodeIdSanitiser.sanitiseNodeId(node.getId());
        final String nodeType = getNodeTypeEnum(node.getType());
        final String transitionName = NodeTransitionName.getTransitionNameForNode(node);
        final String nodeNarrativeId = node.getNarrativeId();
        final String gameStateMutationArray = isEmpty(node.getGameStateModifiers()) ?
                "do_nothing_array" : NodeMutationsArrayName.getMutationArrayForNode(node);
        final int gameStateMutationArraySize = isEmpty(node.getGameStateModifiers()) ?
                1 : node.getGameStateModifiers().size();
        return String.format("struct Node %s = {%s, &%s, &%s, %s, %d};",
                nodeId,
                nodeType,
                transitionName,
                NarrativeName.narrativeName(nodeNarrativeId),
                gameStateMutationArray,
                gameStateMutationArraySize);
    }

    private boolean isEmpty(final List<String> list){
        return list == null || list.isEmpty();
    }

    private String getNodeTypeEnum(final NodeType type) {
        switch (type) {
            case PLAYER:
                return "PLAYER_BASED_TRANSITION";
            case AUTOMATIC:
                return "PREDICATE_BASED_TRANSITION";
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public List<String> dependencies() {
        return List.of(NODE_DEFINITION_RENDERER_NAME,
                NODE_MUTATION_RENDERER_NAME,
                NARRATIVE_RENDERER_NAME,
                TRANSITION_INITIALIZER_RENDERER_NAME,
                DO_NOTHING_MUTATION_ARRAY_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return NODE_RENDERER_NAME;
    }
}
