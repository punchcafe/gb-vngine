package vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.IntegerBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.parse.vngpl.BooleanPredicateStrategy;
import dev.punchcafe.vngine.pom.parse.vngpl.IntegerPredicateStrategy;
import dev.punchcafe.vngine.pom.parse.vngpl.PredicateParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VngIntegerPredicateTest {

    private PredicateParser parser;

    @BeforeEach
    void beforeEach() {
        parser = PredicateParser.withStrategies(new IntegerPredicateStrategy());
    }

    @Test
    void canParseTwoLiteralIntegerPredicates() {
        final var expression = "1 equals 2";
        final var expectedObject = IntegerBiFunction.equals(new IntegerLiteral(1), new IntegerLiteral(2));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void willFailOnMixedLiteralLiteralOperands() {
        final var expression = "1 less_than 'hello''";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void willFailOnMixedLiteralVariableOperands() {
        final var expression = "2 more_than $str.some_var";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void willFailOnMixedVariableLiteralOperands() {
        final var expression = "@int.my_int' less_than 'hello'";
        assertThrows(InvalidVngplExpression.class, () -> parser.parse(expression));
    }

    @Test
    void canParseTwoVariableMoreThanPredicates() {
        final var expression = "@int.my_string more_than $int.mystring";
        final var expectedObject = IntegerBiFunction.moreThan(new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_string"),
                new IntegerGameVariable(GameVariableLevel.GAME, "mystring"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableLessThanPredicates() {
        final var expression = "@int.my_string less_than $int.mystring";
        final var expectedObject = IntegerBiFunction.lessThan(new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_string"),
                new IntegerGameVariable(GameVariableLevel.GAME, "mystring"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableEqualsStringPredicatesWithApostropheAndWhiteSpace() {
        final var expression = " $int.mystring   equals @int.my_string     ";
        final var expectedObject = IntegerBiFunction.equals(new IntegerGameVariable(GameVariableLevel.GAME, "mystring"),
                new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_string"));
        assertEquals(expectedObject, parser.parse(expression));
    }

    @Test
    void canParseTwoVariableWithMixedVariableAndLiteral() {
        final var expression = " $int.mystring   equals 4001     ";
        final var expectedObject = IntegerBiFunction.equals(new IntegerGameVariable(GameVariableLevel.GAME, "mystring"),
                new IntegerLiteral(4001));
        assertEquals(expectedObject, parser.parse(expression));
    }
}
