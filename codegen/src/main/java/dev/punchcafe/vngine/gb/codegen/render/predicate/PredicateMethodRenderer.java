package dev.punchcafe.vngine.gb.codegen.render.predicate;

import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;

public class PredicateMethodRenderer {

    public static String render(final PredicateExpression expression){
        final var stringBuilder = new StringBuilder();
        stringBuilder.append("\nint ")
                .append(PredicateMethodNameConverter.convertPredicateExpression(expression))
                .append("(struct GameState * "+PredicateMethodConstants.GAME_STATE_POINTER_ARG_NAME+")\n{\n")
                .append("    return " + PredicateMethodBodyConverter.convertPredicateExpression(expression) + ";")
                .append("\n}");
        return stringBuilder.toString();
    }
}
