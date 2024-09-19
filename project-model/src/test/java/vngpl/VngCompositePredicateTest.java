package vngpl;

import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.IntegerBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.StringBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.composite.AndOrOperation;
import dev.punchcafe.vngine.pom.model.vngpl.composite.CompositeExpression;
import dev.punchcafe.vngine.pom.model.vngpl.composite.PredicateLink;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import dev.punchcafe.vngine.pom.parse.vngpl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VngCompositePredicateTest {

    private PredicateParser parser;

    @BeforeEach
    void beforeEach() {
        parser = PredicateParser.defaultParser();
    }

    @Test
    void parseExampleOne() {
        final var expression = "true is false AND 2 equals 3 AND 'hello' isnt 'goodbye'";
        final var expectedResult = CompositeExpression.fromLinks(List.of(
                PredicateLink.firstLink(BooleanBiFunction.is(BooleanLiteral.TRUE, BooleanLiteral.FALSE)),
                PredicateLink.newLink(IntegerBiFunction.equals(new IntegerLiteral(2), new IntegerLiteral(3)),
                        AndOrOperation.AND),
                PredicateLink.newLink(StringBiFunction.isnt(new StringLiteral("hello"), new StringLiteral("goodbye")),
                        AndOrOperation.AND)));
        final var result = parser.parse(expression);
        assertEquals(result, expectedResult);
    }

    @Test
    void parseExampleTwo() {
        final var expression = "(true is false AND 2 equals 3 AND 'hello' isnt 'goodbye')";
        final var expectedResult = CompositeExpression.fromLinks(List.of(
                PredicateLink.firstLink(BooleanBiFunction.is(BooleanLiteral.TRUE, BooleanLiteral.FALSE)),
                PredicateLink.newLink(IntegerBiFunction.equals(new IntegerLiteral(2), new IntegerLiteral(3)),
                        AndOrOperation.AND),
                PredicateLink.newLink(StringBiFunction.isnt(new StringLiteral("hello"), new StringLiteral("goodbye")),
                        AndOrOperation.AND)));
        final var result = parser.parse(expression);
        assertEquals(result, expectedResult);
    }

    @Test
    void parseExampleThree() {
        final var expression = "(true is false AND 2 equals 3 AND ('hello' isnt 'goodbye' OR 5 equals 4))";
        final var expectedResult = CompositeExpression.fromLinks(List.of(
                PredicateLink.firstLink(BooleanBiFunction.is(BooleanLiteral.TRUE, BooleanLiteral.FALSE)),
                PredicateLink.newLink(IntegerBiFunction.equals(new IntegerLiteral(2), new IntegerLiteral(3)),
                        AndOrOperation.AND),
                PredicateLink.newLink(
                        CompositeExpression.fromLinks(
                                List.of(PredicateLink.firstLink(StringBiFunction.isnt(new StringLiteral("hello"),
                                        new StringLiteral("goodbye"))),
                                        PredicateLink.newLink(IntegerBiFunction.equals(new IntegerLiteral(5),
                                                new IntegerLiteral(4)), AndOrOperation.OR))),
                        AndOrOperation.AND)));
        final var result = parser.parse(expression);
        assertEquals(result, expectedResult);
    }
}
