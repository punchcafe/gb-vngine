package dev.punchcafe.vngine.gb.codegen.gsmutate;

import dev.punchcafe.vngine.gb.codegen.ComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.gs.GameStateRenderer;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import dev.punchcafe.vngine.pom.model.vngml.GameStateMutationExpression;
import dev.punchcafe.vngine.pom.parse.vngml.GameStateMutationExpressionParser;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@Builder
public class GameStateMutationRenderer implements ComponentRenderer {

    public static final String GAME_STATE_MUTATION_RENDERER = "GAME_STATE_MUTATION_RENDERER";

    private static final String COMPONENT_HEADERS = "\n#ifndef GAME_STATE_MUTATIONS_DEFINITION" +
            "\n#define GAME_STATE_MUTATIONS_DEFINITION\n";
    private static final String COMPONENT_FOOTER = "\n#endif\n";

    private final ProjectObjectModel<?> gameModel;

    @Override
    public String render() {
        final var mutationExpressionParser = new GameStateMutationExpressionParser();
        final var expressionToCParser = new GameStateMutationExpressionRenderer();
        // Assume it has been validated
        return gameModel.getChapterConfigs().get(0).getNodes().stream()
                .map(Node::getGameStateModifiers)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(mutationExpressionParser::parseExpression)
                .map(expression -> expression.acceptVisitor(expressionToCParser))
                .collect(joining("\n", COMPONENT_HEADERS, COMPONENT_FOOTER));
    }

    @Override
    public List<String> dependencies() {
        return List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return GAME_STATE_MUTATION_RENDERER;
    }
}
