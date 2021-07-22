package dev.punchcafe.vngine.gb.codegen.predicate;

import dev.punchcafe.vngine.gb.codegen.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.*;
import dev.punchcafe.vngine.pom.parse.vngpl.PredicateParser;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.ComponentRendererName.UTILITY_METHOD_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.gs.GameStateRenderer.GAME_STATE_RENDERER_NAME;

@Builder
public class PredicatesRenderer implements ComponentRenderer {

    public static String PREDICATES_RENDERER_NAME = "PREDICATES_RENDERER";

    private final ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        final ChapterConfig chapterConfig = gameConfig.getChapterConfigs().get(0);
        return chapterConfig.getNodes().stream()
                .filter(node -> node.getType() == NodeType.AUTOMATIC)
                .map(Node::getBranches)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(Branch::getPredicateExpression)
                .distinct()
                .map(PredicateParser.defaultParser()::parse)
                .map(PredicateMethodRenderer::render)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<String> dependencies() {
        return List.of(GAME_STATE_RENDERER_NAME, UTILITY_METHOD_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return PREDICATES_RENDERER_NAME;
    }
}
