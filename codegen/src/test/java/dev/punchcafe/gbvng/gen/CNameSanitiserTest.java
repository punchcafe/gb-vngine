package dev.punchcafe.gbvng.gen;

import dev.punchcafe.gbvng.gen.csan.CNameSanitiser;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CNameSanitiserTest {

    @Test
    void shouldSanitiseGameIntVariableName() {
        final var result = CNameSanitiser.sanitiseVariableName("my-legal-variable",
                VariableTypes.INT,
                GameVariableLevel.GAME);
        assertEquals(result, "game_var_int_my_legal_variable");
    }

    @Test
    void shouldSanitiseChapterIntVariableName() {
        final var result = CNameSanitiser.sanitiseVariableName("my-legal-variable",
                VariableTypes.INT,
                GameVariableLevel.CHAPTER);
        assertEquals(result, "chapter_var_int_my_legal_variable");
    }

    @Test
    void shouldSanitiseGameStrVariableName() {
        final var result = CNameSanitiser.sanitiseVariableName("my-legal-variable",
                VariableTypes.STR,
                GameVariableLevel.GAME);
        assertEquals(result, "game_var_str_my_legal_variable");
    }

    @Test
    void shouldSanitiseChapterStrVariableName() {
        final var result = CNameSanitiser.sanitiseVariableName("my-legal-variable",
                VariableTypes.STR,
                GameVariableLevel.CHAPTER);
        assertEquals(result, "chapter_var_str_my_legal_variable");
    }

    @Test
    void shouldSanitiseGameBoolVariableName() {
        final var result = CNameSanitiser.sanitiseVariableName("my-legal-variable",
                VariableTypes.BOOL,
                GameVariableLevel.GAME);
        assertEquals(result, "game_var_bool_my_legal_variable");
    }

    @Test
    void shouldSanitiseChapterBoolVariableName() {
        final var result = CNameSanitiser.sanitiseVariableName("my-legal-variable",
                VariableTypes.BOOL,
                GameVariableLevel.CHAPTER);
        assertEquals(result, "chapter_var_bool_my_legal_variable");
    }

    @Test
    void shouldSanitiseChapterBoolVariableNameWithNumbers() {
        final var result = CNameSanitiser.sanitiseVariableName("my-1-variable",
                VariableTypes.BOOL,
                GameVariableLevel.CHAPTER);
        assertEquals(result, "chapter_var_bool_my_1_variable");
    }

    @Test
    void shouldThrowIllegalArgumentOperationWhenInvalidCharacters() {
        assertThrows(IllegalArgumentException.class,
                () -> CNameSanitiser.sanitiseVariableName("my-!llegal-variable",
                        VariableTypes.INT,
                        GameVariableLevel.GAME));
    }

    @Test
    void shouldThrowIllegalArgumentOperationWhenDashAtFront() {
        assertThrows(IllegalArgumentException.class,
                () -> CNameSanitiser.sanitiseVariableName("-my-illegal-variable",
                        VariableTypes.INT,
                        GameVariableLevel.GAME));
    }
}
