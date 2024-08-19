package dev.punchcafe.gbvng.gen.render.mutate;

import dev.punchcafe.gbvng.gen.adapter.NodeMutationsArrayName;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import dev.punchcafe.vngine.pom.parse.vngml.GameStateMutationExpressionParser;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.NODE_MUTATION_RENDERER_NAME;
import static dev.punchcafe.gbvng.gen.render.mutate.GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER;
import static java.util.stream.Collectors.joining;

@Builder
public class NodeMutationsRenderers implements ComponentRenderer {

    private static final GameStateMutationExpressionParser mutationParser = new GameStateMutationExpressionParser();

    private final ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        return gameConfig.getChapterConfigs().get(0)
                .getNodes()
                .stream()
                .map(this::renderNodeMutations)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<String> dependencies() {
        return List.of(GAME_STATE_MUTATION_RENDERER);
    }

    @Override
    public String componentName() {
        return NODE_MUTATION_RENDERER_NAME;
    }

    private String renderNodeMutations(final Node node) {

        if(node.getGameStateModifiers() == null || node.getGameStateModifiers().isEmpty()){
            return "";
        }

        final var mutationArrayPointerName = NodeMutationsArrayName.getMutationArrayForNode(node);
        final var methodReferencesArray = node.getGameStateModifiers().stream()
                .map(mutationParser::parseExpression)
                .map(MutationMethodNameConverter::convert)
                .map(methodName -> "&" + methodName)
                .collect(joining(",", "{", "}"));
        final var numberOfMutations = node.getGameStateModifiers().size();

        return String.format("void (*%s[%d])(struct GameState *) = %s;", mutationArrayPointerName,
                numberOfMutations,
                methodReferencesArray);
    }

}
