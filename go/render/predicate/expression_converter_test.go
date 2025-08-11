package predicate

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services/predicate"
)

var emptyGS = project.GameState{}

func TestExpressionConverter(t *testing.T) {
	t.Run("it renders literals ", func(t *testing.T) {
		gs := project.GameState{"a_bool_var": project.BOOL}
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.BoolLiteral(false): "false",
			predicate.BoolLiteral(true):  "true",
			// Converter shouldn't validate return type.
			predicate.NumberLiteral(42):               "42",
			predicate.VariableReference("a_bool_var"): "game_state->a_bool_var",
			// Longer term we should use a constant ref reference here
			predicate.StringLiteral("string_value"): "\"string_value\"",
		} {
			res, err := ConvertExpressionToSource(expression, gs)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders bracket operations ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Brackets{predicate.BoolLiteral(true)}: "(true)",
		} {
			res, err := ConvertExpressionToSource(expression, emptyGS)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders AND operations ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.And{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}:                     "true && false",
			predicate.And{predicate.Brackets{predicate.BoolLiteral(true)}, predicate.BoolLiteral(false)}: "(true) && false",
		} {
			res, err := ConvertExpressionToSource(expression, emptyGS)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders OR operations ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Or{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}:                     "true || false",
			predicate.Or{predicate.Brackets{predicate.BoolLiteral(true)}, predicate.BoolLiteral(false)}: "(true) || false",
		} {
			res, err := ConvertExpressionToSource(expression, emptyGS)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders EQUALS operations ", func(t *testing.T) {
		gs := project.GameState{"my_string": project.STRING, "my_num": project.INT}

		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Equal{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}:                     "true == false",
			predicate.Equal{predicate.Brackets{predicate.BoolLiteral(true)}, predicate.BoolLiteral(false)}: "(true) == false",
			predicate.Equal{predicate.NumberLiteral(25), predicate.NumberLiteral(25)}:                      "25 == 25",
			predicate.Equal{predicate.VariableReference("my_num"), predicate.NumberLiteral(123)}:           "game_state->my_num == 123",
			// String comparisons need to use the string comparator
			predicate.Equal{predicate.StringLiteral("hello"), predicate.StringLiteral("world")}:         "str_compare(\"hello\", \"world\")",
			predicate.Equal{predicate.StringLiteral("hello"), predicate.VariableReference("my_string")}: "str_compare(\"hello\", game_state->my_string)",
		} {
			res, err := ConvertExpressionToSource(expression, gs)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders MORE THAN operations ", func(t *testing.T) {
		gs := project.GameState{"my_num": project.INT}
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.MoreThan{predicate.NumberLiteral(25), predicate.NumberLiteral(20)}:            "25 > 20",
			predicate.MoreThan{predicate.VariableReference("my_num"), predicate.NumberLiteral(123)}: "game_state->my_num > 123",
		} {
			res, err := ConvertExpressionToSource(expression, gs)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders LESS THAN operations ", func(t *testing.T) {
		gs := project.GameState{"my_num": project.INT}
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.LessThan{predicate.NumberLiteral(25), predicate.NumberLiteral(20)}:            "25 < 20",
			predicate.LessThan{predicate.VariableReference("my_num"), predicate.NumberLiteral(123)}: "game_state->my_num < 123",
		} {
			res, err := ConvertExpressionToSource(expression, gs)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})
}
