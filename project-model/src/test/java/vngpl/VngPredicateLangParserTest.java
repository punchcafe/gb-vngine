package vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.VngPLParser;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VngPredicateLangParserTest {

    @Test
    void shouldParseIntegerGameVariable() {
        final var expression = "$int.my_variable";
        final var expectedObject = new IntegerGameVariable(GameVariableLevel.GAME, "my_variable");
        assertEquals(VngPLParser.parseAtomicIntegerVariable(expression), expectedObject);
    }

    @Test
    void shouldParseIntegerChapterVariable() {
        final var expression = "@int.my_variable";
        final var expectedObject = new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_variable");
        assertEquals(VngPLParser.parseAtomicIntegerVariable(expression), expectedObject);
    }

    @Test
    void shouldNotParseIntegerGameVariableWhichDoesntMatch() {
        final var expression = "$int.my_variable  someextra";
        assertThrows(InvalidVngplExpression.class, () -> VngPLParser.parseAtomicIntegerVariable(expression));
    }

    @Test
    void shouldConvertIntegerGameVariableToVngpl() {
        final var expression = "$int.my_variable";
        final var object = new IntegerGameVariable(GameVariableLevel.GAME, "my_variable");
        assertEquals(object.asVngQL(), expression);
    }

    @Test
    void shouldParseIntegerLiteral() {
        final var expression = "15";
        final var expectedObject = new IntegerLiteral(15);
        assertEquals(VngPLParser.parseAtomicIntegerVariable(expression), expectedObject);
    }

    @Test
    void shouldNotParseIntegerVariableIfJibberish() {
        final var expression = "fihufeiru";
        assertThrows(InvalidVngplExpression.class, () -> VngPLParser.parseAtomicIntegerVariable(expression));
    }

    @Test
    void shouldParseStringGameVariable() {
        final var expression = "$str.my_variable";
        final var expectedObject = new StringGameVariable(GameVariableLevel.GAME, "my_variable");
        assertEquals(VngPLParser.parseAtomicStringVariable(expression), expectedObject);
    }

    @Test
    void shouldParseStringGameVariableIgnoringWhitespace() {
        final var expression = "      $str.my_variable  ";
        final var expectedObject = new StringGameVariable(GameVariableLevel.GAME, "my_variable");
        assertEquals(VngPLParser.parseAtomicStringVariable(expression), expectedObject);
    }

    @Test
    void shouldParseStringChapterVariable() {
        final var expression = "@str.my_variable";
        final var expectedObject = new StringGameVariable(GameVariableLevel.CHAPTER, "my_variable");
        assertEquals(VngPLParser.parseAtomicStringVariable(expression), expectedObject);
    }

    @Test
    void shouldParseStringLiteral() {
        final var expression = "'my_variable'";
        final var expectedObject = new StringLiteral("my_variable");
        assertEquals(VngPLParser.parseAtomicStringVariable(expression), expectedObject);
    }

    @Test
    void shouldNotParseStringLiteralIfNoQuotes() {
        final var expression = "my_variable";
        assertThrows(InvalidVngplExpression.class, () -> VngPLParser.parseAtomicStringVariable(expression));
    }

    @Test
    void shouldParseBooleanGameVariable() {
        final var expression = "$bool.my_variable";
        final var expectedObject = new BoolGameVariable(GameVariableLevel.GAME, "my_variable");
        assertEquals(expectedObject, VngPLParser.parseAtomicBooleanVariable(expression));
    }

    @Test
    void shouldParseBooleanChapterVariable() {
        final var expression = "@bool.my_variable";
        final var expectedObject = new BoolGameVariable(GameVariableLevel.CHAPTER, "my_variable");
        assertEquals(expectedObject, VngPLParser.parseAtomicBooleanVariable(expression));
    }

    @Test
    void shouldParseTrueBooleanLiteral() {
        final var expression = "true";
        assertEquals(BooleanLiteral.TRUE, VngPLParser.parseAtomicBooleanVariable(expression));
    }

    @Test
    void shouldParseFalseBooleanLiteral() {
        final var expression = "false";
        assertEquals(BooleanLiteral.FALSE, VngPLParser.parseAtomicBooleanVariable(expression));
    }

    @Test
    void shouldParseFalseBooleanLiteralWithWhitespace() {
        final var expression = " false   ";
        assertEquals(BooleanLiteral.FALSE, VngPLParser.parseAtomicBooleanVariable(expression));
    }

    @Test
    void shouldNotParseNonsenseBooleanLiteral() {
        final var expression = "kuehdkwube";
        assertThrows(InvalidVngplExpression.class, () -> VngPLParser.parseAtomicBooleanVariable(expression));
    }
}
