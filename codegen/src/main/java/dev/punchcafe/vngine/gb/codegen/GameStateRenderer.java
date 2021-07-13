package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.pom.model.GameStateVariableConfig;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
public class GameStateRenderer {

    private static String DEFINITION_HEADERS = "" +
            "#ifndef GAMESTATE_TYPE_DEFINITION\n" +
            "#define GAMESTATE_TYPE_DEFINITION\n";
    private static String STRUCT_STATEMENT_OPENER = "struct GameState {\n";
    private static String STRUCT_STATEMENT_CLOSER = "};\n";
    private static String GLOBAL_GAMESTATE_TEMPLATE = "struct GameState %s;";

    private final String globalGameStateVariableName;
    private final int maxStringVariableLength;
    private final GameStateVariableConfig gameStateVariableConfig;

    public String render() {
        return new StringBuilder()
                .append(DEFINITION_HEADERS)
                .append(STRUCT_STATEMENT_OPENER)
                .append(this.renderGameStateContents())
                .append(STRUCT_STATEMENT_CLOSER)
                .append(this.renderGameStateSingleton())
                .toString();
    }

    private String renderGameStateContents() {
        return this.gameStateVariableConfig
                .getGameStateVariables()
                .entrySet()
                .stream()
                .map(this::renderVariableOfType)
                .map(variable -> RenderUtils.addIndentationOfLevel(variable, 1))
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private String renderVariableOfType(final Map.Entry<String, VariableTypes> variableEntry) {
        switch (variableEntry.getValue()) {
            case BOOL:
                return String.format("short %s;", variableEntry.getKey());
            case STR:
                return String.format("char %s [%d];", variableEntry.getKey(), this.maxStringVariableLength);
            case INT:
                return String.format("int %s;", variableEntry.getKey());
            default:
                throw new UnsupportedOperationException();
        }
    }

    private String renderGameStateSingleton() {
        return String.format(GLOBAL_GAMESTATE_TEMPLATE, this.globalGameStateVariableName);
    }
    /*
    TODO:
    consider a List<Class<? extends Renderer>> getDependencies() for rendering everything into one big c file in the right order?
     */
}
