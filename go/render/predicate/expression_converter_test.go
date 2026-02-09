package predicate

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
	e "punchcafe.dev/gb-vngine/services/expression"
)

var emptyGS = project.GameState{}
var emptyStringRegistry = services.NewRegistry()

func TestExpressionConverter(t *testing.T) {
	t.Run("it renders literals ", func(t *testing.T) {
		gs := project.GameState{"a_bool_var": project.BOOL}
		sr := services.NewRegistry()
		sr.AddString("string_value")
		for expression, expectedName := range map[e.Expression]string{
			e.BoolLiteral(false): "false",
			e.BoolLiteral(true):  "true",
			// Converter shouldn't validate return type.
			e.NumberLiteral(42):               "42",
			e.VariableReference("a_bool_var"): "game_state->a_bool_var",
			// Longer term we should use a constant ref reference here
			e.StringLiteral("string_value"): "STRING_REG_1",
		} {
			res, err := ConvertExpressionToSource(expression, gs, &sr)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders bracket operations ", func(t *testing.T) {
		for expression, expectedName := range map[e.Expression]string{
			e.Brackets{e.BoolLiteral(true)}: "(true)",
		} {
			sr := services.NewRegistry()
			res, err := ConvertExpressionToSource(expression, emptyGS, &sr)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders AND operations ", func(t *testing.T) {
		for expression, expectedName := range map[e.Expression]string{
			e.And{e.BoolLiteral(true), e.BoolLiteral(false)}:             "true && false",
			e.And{e.Brackets{e.BoolLiteral(true)}, e.BoolLiteral(false)}: "(true) && false",
		} {
			res, err := ConvertExpressionToSource(expression, emptyGS, &emptyStringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders OR operations ", func(t *testing.T) {
		for expression, expectedName := range map[e.Expression]string{
			e.Or{e.BoolLiteral(true), e.BoolLiteral(false)}:             "true || false",
			e.Or{e.Brackets{e.BoolLiteral(true)}, e.BoolLiteral(false)}: "(true) || false",
		} {
			res, err := ConvertExpressionToSource(expression, emptyGS, &emptyStringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders EQUALS operations ", func(t *testing.T) {
		gs := project.GameState{"my_string": project.STRING, "my_num": project.INT}
		sr := services.NewRegistry()
		sr.AddString("hello")
		sr.AddString("world")

		for expression, expectedName := range map[e.Expression]string{
			e.Equal{e.BoolLiteral(true), e.BoolLiteral(false)}:             "true == false",
			e.Equal{e.Brackets{e.BoolLiteral(true)}, e.BoolLiteral(false)}: "(true) == false",
			e.Equal{e.NumberLiteral(25), e.NumberLiteral(25)}:              "25 == 25",
			e.Equal{e.VariableReference("my_num"), e.NumberLiteral(123)}:   "game_state->my_num == 123",
			// String comparisons need to use the string comparator
			e.Equal{e.StringLiteral("hello"), e.StringLiteral("world")}:         "(strcmp(STRING_REG_1, STRING_REG_2) == 0)",
			e.Equal{e.StringLiteral("hello"), e.VariableReference("my_string")}: "(strcmp(STRING_REG_1, game_state->my_string) == 0)",
		} {
			res, err := ConvertExpressionToSource(expression, gs, &sr)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders MORE THAN operations ", func(t *testing.T) {
		gs := project.GameState{"my_num": project.INT}
		for expression, expectedName := range map[e.Expression]string{
			e.MoreThan{e.NumberLiteral(25), e.NumberLiteral(20)}:            "25 > 20",
			e.MoreThan{e.VariableReference("my_num"), e.NumberLiteral(123)}: "game_state->my_num > 123",
		} {
			res, err := ConvertExpressionToSource(expression, gs, &emptyStringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders LESS THAN operations ", func(t *testing.T) {
		gs := project.GameState{"my_num": project.INT}
		for expression, expectedName := range map[e.Expression]string{
			e.LessThan{e.NumberLiteral(25), e.NumberLiteral(20)}:            "25 < 20",
			e.LessThan{e.VariableReference("my_num"), e.NumberLiteral(123)}: "game_state->my_num < 123",
		} {
			res, err := ConvertExpressionToSource(expression, gs, &emptyStringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})
}
