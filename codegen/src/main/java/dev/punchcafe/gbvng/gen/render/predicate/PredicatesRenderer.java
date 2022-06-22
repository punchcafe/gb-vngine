package dev.punchcafe.gbvng.gen.render.predicate;

import dev.punchcafe.gbvng.gen.predicate.PredicateService;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.ComponentRendererNames;
import dev.punchcafe.gbvng.gen.render.gs.GameStateRenderer;
import dev.punchcafe.gbvng.gen.shared.SourceName;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class PredicatesRenderer implements ComponentRenderer {

    public static String PREDICATES_RENDERER_NAME = "PREDICATES_RENDERER";

    private final PredicateService predicateService;

    @Override
    public String render() {
        return predicateService
                .allPredicateFunctions()
                .map(this::renderPredicateMethod)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<String> dependencies() {
        return List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME, ComponentRendererNames.UTILITY_METHOD_RENDERER_NAME);
    }

    private String renderPredicateMethod(final Map.Entry<SourceName, PredicateExpression> expression){
        final var stringBuilder = new StringBuilder();
        stringBuilder.append("\nint ")
                .append(expression.getKey().toString())
                .append("(struct GameState * "+PredicateMethodConstants.GAME_STATE_POINTER_ARG_NAME+")\n{\n")
                .append("    return " + PredicateMethodBodyConverter.convertPredicateExpression(expression.getValue()) + ";")
                .append("\n}");
        return stringBuilder.toString();
    }

    @Override
    public String componentName() {
        return PREDICATES_RENDERER_NAME;
    }
}
