package vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.parse.vngpl.BooleanPredicateStrategy;
import dev.punchcafe.vngine.pom.parse.vngpl.PredicateParser;
import dev.punchcafe.vngine.pom.parse.vngpl.StringPredicateStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VngBooleanPredicateTest {

    private PredicateParser parser;

    @BeforeEach
    void beforeEach() {
        parser = PredicateParser.withStrategies(new BooleanPredicateStrategy());
    }

    @Test
    void canParseTwoLiteralStringPredicates() {
        final var expression = "true is false";
        final var expectedObject = BooleanBiFunction.is(BooleanLiteral.TRUE, BooleanLiteral.FALSE);
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void willFailOnMixedLiteralLiteralOperands() {
        final var expression = "false is 'hello''";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void willFailOnMixedLiteralVariableOperands() {
        final var expression = "true is $str.some_var";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void willFailOnMixedVariableLiteralOperands() {
        final var expression = "@bool.my_string' is 'hello'";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void canParseTwoVariableIsntStringPredicates() {
        final var expression = "@bool.my_string isnt $bool.mystring";
        final var expectedObject = BooleanBiFunction.isnt(new BoolGameVariable(GameVariableLevel.CHAPTER, "my_string"),
                new BoolGameVariable(GameVariableLevel.GAME, "mystring"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableIsntStringPredicatesWithApostropheAndWhiteSpace() {
        final var expression = " $bool.mystring   isn't @bool.my_string     ";
        final var expectedObject = BooleanBiFunction.isnt(new BoolGameVariable(GameVariableLevel.GAME, "mystring"),
                new BoolGameVariable(GameVariableLevel.CHAPTER, "my_string"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableWithMixedVariableAndLiteral() {
        final var expression = " $bool.mystring   isn't false     ";
        final var expectedObject = BooleanBiFunction.isnt(new BoolGameVariable(GameVariableLevel.GAME, "mystring"),
                BooleanLiteral.FALSE);
        assertEquals(expectedObject, parser.parse(expression));
    }
}
