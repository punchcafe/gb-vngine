package vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.StringBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import dev.punchcafe.vngine.pom.parse.vngpl.PredicateParser;
import dev.punchcafe.vngine.pom.parse.vngpl.StringPredicateStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VngStringPredicateTest {

    private PredicateParser parser;

    @BeforeEach
    void beforeEach() {
        parser = PredicateParser.withStrategies(new StringPredicateStrategy());
    }

    @Test
    void canParseTwoLiteralStringPredicates() {
        final var expression = "'my_string' is 'mystring'";
        final var expectedObject = StringBiFunction.is(new StringLiteral("my_string"), new StringLiteral("mystring"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void willFailOnMixedLiteralLiteralOperands() {
        final var expression = "'my_string' is 10'";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void willFailOnMixedLiteralVariableOperands() {
        final var expression = "'my_string' is $int.some_var";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void willFailOnMixedVariableLiteralOperands() {
        final var expression = "@str.my_string' is 15";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void canParseTwoVariableIsntStringPredicates() {
        final var expression = "@str.my_string isnt $str.mystring";
        final var expectedObject = StringBiFunction.isnt(new StringGameVariable(GameVariableLevel.CHAPTER, "my_string"),
                new StringGameVariable(GameVariableLevel.GAME, "mystring"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableIsntStringPredicatesWithApostropheAndWhiteSpace() {
        final var expression = " $str.mystring   isn't @str.my_string     ";
        final var expectedObject = StringBiFunction.isnt(new StringGameVariable(GameVariableLevel.GAME, "mystring"),
                new StringGameVariable(GameVariableLevel.CHAPTER, "my_string"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableWithMixedVariableAndLiteral() {
        final var expression = " $str.mystring   isn't 'hello world'     ";
        final var expectedObject = StringBiFunction.isnt(new StringGameVariable(GameVariableLevel.GAME, "mystring"),
                new StringLiteral("hello world"));
        assertEquals(expectedObject, parser.parse(expression));
    }
}
