package dev.punchcafe.vngine.gb.codegen.gs;

import dev.punchcafe.vngine.gb.codegen.ComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.RenderUtils;
import dev.punchcafe.vngine.gb.codegen.csan.CVariableNameSanitiser;
import dev.punchcafe.vngine.pom.model.GameStateVariableConfig;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
public class GameStateRenderer implements ComponentRenderer {

    public static String GAME_STATE_RENDERER_NAME = "GAME_STATE_RENDERER_NAME";

    private static String DEFINITION_HEADERS = "" +
            "#ifndef GAMESTATE_TYPE_DEFINITION\n" +
            "#define GAMESTATE_TYPE_DEFINITION\n";

    private static String DEFINITION_FOOTER = "\n#endif";
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
                .append(DEFINITION_FOOTER)
                .toString();
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return GAME_STATE_RENDERER_NAME;
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
        final var variableName = CVariableNameSanitiser.sanitiseVariableName(variableEntry.getKey(),
                variableEntry.getValue(),
                GameVariableLevel.GAME);
        switch (variableEntry.getValue()) {
            case BOOL:
                return String.format("short %s;", variableName);
            case STR:
                return String.format("char %s [%d];", variableName, this.maxStringVariableLength);
            case INT:
                return String.format("int %s;", variableName);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private String renderGameStateSingleton() {
        return String.format(GLOBAL_GAMESTATE_TEMPLATE, this.globalGameStateVariableName);
    }
}
