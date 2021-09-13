package dev.punchcafe.gbvng.gen.render.predicate;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.ComponentRendererName;
import dev.punchcafe.gbvng.gen.render.gs.GameStateRenderer;
import dev.punchcafe.vngine.pom.model.*;
import dev.punchcafe.vngine.pom.parse.vngpl.PredicateParser;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME, ComponentRendererName.UTILITY_METHOD_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return PREDICATES_RENDERER_NAME;
    }
}
