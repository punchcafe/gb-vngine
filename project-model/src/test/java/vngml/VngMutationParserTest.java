package vngml;

import dev.punchcafe.vngine.pom.model.vngml.DecreaseIntegerMutation;
import dev.punchcafe.vngine.pom.model.vngml.IncreaseIntegerMutation;
import dev.punchcafe.vngine.pom.model.vngml.SetBooleanMutation;
import dev.punchcafe.vngine.pom.model.vngml.SetStringMutation;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import dev.punchcafe.vngine.pom.parse.vngml.GameStateMutationExpressionParser;
import dev.punchcafe.vngine.pom.parse.vngml.InvalidVngmlExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VngMutationParserTest {

    private GameStateMutationExpressionParser parser;

    @BeforeEach
    void beforeEach(){
        this.parser = new GameStateMutationExpressionParser();
    }

    @Test
    void shouldParseIncreaseGameLevelIntegerMutation(){
        final var expression = "increase $int.my_integer_var by 5";
        final var expectedMutation = IncreaseIntegerMutation.builder()
                .increaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.GAME, "my_integer_var"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForIncreaseGameLevelIntegerMutation(){
        final var expectedExpression = "increase $int.my_integer_var by 5";
        final var mutation = IncreaseIntegerMutation.builder()
                .increaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.GAME, "my_integer_var"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldParseIncreaseChapterLevelIntegerMutation(){
        final var expression = "increase @int.my_integer_var by 5";
        final var expectedMutation = IncreaseIntegerMutation.builder()
                .increaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_integer_var"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForIncreaseChapterLevelIntegerMutation(){
        final var expectedExpression = "increase @int.my_integer_var by 5";
        final var mutation = IncreaseIntegerMutation.builder()
                .increaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_integer_var"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldThrowInvalidVngExceptionWhenMismatchOfLiteralsOnChapterLevelIntegerIncrease(){
        final var expression = "increase @int.my_integer_var by false";
        assertThrows(InvalidVngmlExpression.class, () -> parser.parseExpression(expression));
    }

    @Test
    void shouldThrowInvalidVngExceptionWhenMismatchOfLiteralsOnGameLevelIntegerIncrease(){
        final var expression = "increase $int.my_integer_var by 'hello world'";
        assertThrows(InvalidVngmlExpression.class, () -> parser.parseExpression(expression));
    }

    @Test
    void shouldThrowInvalidVngExceptionWhenMismatchOfOperationOnGameLevelIntegerIncrease(){
        final var expression = "increase $int.my_integer_var to 5";
        assertThrows(InvalidVngmlExpression.class, () -> parser.parseExpression(expression));
    }

    @Test
    void shouldParseDecreaseGameLevelIntegerMutation(){
        final var expression = "decrease $int.my_integer_var by 5";
        final var expectedMutation = DecreaseIntegerMutation.builder()
                .decreaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.GAME, "my_integer_var"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForDecreaseGameLevelIntegerMutation(){
        final var expectedExpression = "decrease $int.my_integer_var by 5";
        final var mutation = DecreaseIntegerMutation.builder()
                .decreaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.GAME, "my_integer_var"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldParseDecreaseChapterLevelIntegerMutation(){
        final var expression = "decrease @int.my_integer_var by 5";
        final var expectedMutation = DecreaseIntegerMutation.builder()
                .decreaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_integer_var"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForDecreaseChapterLevelIntegerMutation(){
        final var expectedExpression = "decrease @int.my_integer_var by 5";
        final var mutation = DecreaseIntegerMutation.builder()
                .decreaseBy(new IntegerLiteral(5))
                .variableToModify(new IntegerGameVariable(GameVariableLevel.CHAPTER, "my_integer_var"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldThrowInvalidVngExceptionWhenMismatchOfLiteralsOnChapterLevelIntegerDecrease(){
        final var expression = "decrease @int.my_integer_var by false";
        assertThrows(InvalidVngmlExpression.class, () -> parser.parseExpression(expression));
    }

    @Test
    void shouldThrowInvalidVngExceptionWhenMismatchOfLiteralsOnGameLevelIntegerDecrease(){
        final var expression = "decrease $int.my_integer_var by 'hello world'";
        assertThrows(InvalidVngmlExpression.class, () -> parser.parseExpression(expression));
    }

    @Test
    void shouldThrowInvalidVngExceptionWhenMismatchOfOperationOnGameLevelIntegerDecrease(){
        final var expression = "decrease $int.my_integer_var to 5";
        assertThrows(InvalidVngmlExpression.class, () -> parser.parseExpression(expression));
    }

    @Test
    void shouldParseSetGameLevelStringMutation(){
        final var expression = "set $str.my_string to 'hello, world!'";
        final var expectedMutation = SetStringMutation.builder()
                .changeValue(new StringLiteral("hello, world!"))
                .variableToMutate(new StringGameVariable(GameVariableLevel.GAME, "my_string"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForSetGameLevelStringMutation(){
        final var expectedExpression = "set $str.my_string to 'hello, world!'";
        final var mutation = SetStringMutation.builder()
                .changeValue(new StringLiteral("hello, world!"))
                .variableToMutate(new StringGameVariable(GameVariableLevel.GAME, "my_string"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldParseSetChapterLevelStringMutation(){
        final var expression = "set @str.my_string to 'hello, world!'";
        final var expectedMutation = SetStringMutation.builder()
                .changeValue(new StringLiteral("hello, world!"))
                .variableToMutate(new StringGameVariable(GameVariableLevel.CHAPTER, "my_string"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForSetChapterLevelStringMutation(){
        final var expectedExpression = "set @str.my_string to 'hello, world!'";
        final var mutation = SetStringMutation.builder()
                .changeValue(new StringLiteral("hello, world!"))
                .variableToMutate(new StringGameVariable(GameVariableLevel.CHAPTER, "my_string"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldParseSetGameLevelBooleanMutation(){
        final var expression = "set $bool.my_bool to false";
        final var expectedMutation = SetBooleanMutation.builder()
                .changeValue(BooleanLiteral.FALSE)
                .variableToMutate(new BoolGameVariable(GameVariableLevel.GAME, "my_bool"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForSetGameLevelBooleanMutation(){
        final var expectedExpression = "set $bool.my_bool to false";
        final var mutation = SetBooleanMutation.builder()
                .changeValue(BooleanLiteral.FALSE)
                .variableToMutate(new BoolGameVariable(GameVariableLevel.GAME, "my_bool"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }

    @Test
    void shouldParseSetChapterLevelBooleanMutation(){
        final var expression = "set @bool.my_bool to true";
        final var expectedMutation = SetBooleanMutation.builder()
                .changeValue(BooleanLiteral.TRUE)
                .variableToMutate(new BoolGameVariable(GameVariableLevel.CHAPTER, "my_bool"))
                .build();
        final var parsedResult = parser.parseExpression(expression);
        assertEquals(parsedResult, expectedMutation);
    }

    @Test
    void shouldReturnCorrectVngSLForSetChapterLevelBooleanMutation(){
        final var expectedExpression = "set @bool.my_bool to true";
        final var mutation = SetBooleanMutation.builder()
                .changeValue(BooleanLiteral.TRUE)
                .variableToMutate(new BoolGameVariable(GameVariableLevel.CHAPTER, "my_bool"))
                .build();
        assertEquals(expectedExpression, mutation.asVngQL());
    }
}
