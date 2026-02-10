package expression

import (
	"testing"

	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"

	"github.com/stretchr/testify/assert"
)

var emptyGS = project.GameState{}
var emptyStringRegistry = services.NewRegistry()

func TestExpressionConverter(t *testing.T) {
	t.Run("it renders literals ", func(t *testing.T) {
		gs := project.GameState{"a_bool_var": project.BOOL}
		sr := services.NewRegistry()
		sr.AddString("string_value")
		es := &Service{gs: gs, sr: &sr}
		for expression, expectedName := range map[Expression]string{
			BoolLiteral(false): "false",
			BoolLiteral(true):  "true",
			// Converter shouldn't validate return typ
			NumberLiteral(42):               "42",
			VariableReference("a_bool_var"): "game_state->a_bool_var",
			// Longer term we should use a constant ref reference here
			StringLiteral("string_value"): "STRING_REG_1",
		} {
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders bracket operations ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			Brackets{BoolLiteral(true)}: "(true)",
		} {
			sr := services.NewRegistry()
			es := &Service{gs: emptyGS, sr: &sr}
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders AND operations ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			And{BoolLiteral(true), BoolLiteral(false)}:           "true && false",
			And{Brackets{BoolLiteral(true)}, BoolLiteral(false)}: "(true) && false",
		} {
			es := &Service{gs: emptyGS, sr: &emptyStringRegistry}
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders OR operations ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			Or{BoolLiteral(true), BoolLiteral(false)}:           "true || false",
			Or{Brackets{BoolLiteral(true)}, BoolLiteral(false)}: "(true) || false",
		} {
			es := &Service{gs: emptyGS, sr: &emptyStringRegistry}
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders EQUALS operations ", func(t *testing.T) {
		gs := project.GameState{"my_string": project.STRING, "my_num": project.INT}
		sr := services.NewRegistry()
		sr.AddString("hello")
		sr.AddString("world")
		es := &Service{gs: gs, sr: &sr}

		for expression, expectedName := range map[Expression]string{
			Equal{BoolLiteral(true), BoolLiteral(false)}:           "true == false",
			Equal{Brackets{BoolLiteral(true)}, BoolLiteral(false)}: "(true) == false",
			Equal{NumberLiteral(25), NumberLiteral(25)}:            "25 == 25",
			Equal{VariableReference("my_num"), NumberLiteral(123)}: "game_state->my_num == 123",
			// String comparisons need to use the string comparator
			Equal{StringLiteral("hello"), StringLiteral("world")}:         "(strcmp(STRING_REG_1, STRING_REG_2) == 0)",
			Equal{StringLiteral("hello"), VariableReference("my_string")}: "(strcmp(STRING_REG_1, game_state->my_string) == 0)",
		} {
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders MORE THAN operations ", func(t *testing.T) {
		gs := project.GameState{"my_num": project.INT}
		for expression, expectedName := range map[Expression]string{
			MoreThan{NumberLiteral(25), NumberLiteral(20)}:            "25 > 20",
			MoreThan{VariableReference("my_num"), NumberLiteral(123)}: "game_state->my_num > 123",
		} {
			es := &Service{gs: gs, sr: &emptyStringRegistry}
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it renders LESS THAN operations ", func(t *testing.T) {
		gs := project.GameState{"my_num": project.INT}
		for expression, expectedName := range map[Expression]string{
			LessThan{NumberLiteral(25), NumberLiteral(20)}:            "25 < 20",
			LessThan{VariableReference("my_num"), NumberLiteral(123)}: "game_state->my_num < 123",
		} {
			es := &Service{gs: gs, sr: &emptyStringRegistry}
			res, err := es.ConvertExpressionToSource(expression)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})
}
