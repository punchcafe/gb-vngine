package expression

import (
	"fmt"
	"testing"

	"punchcafe.dev/gb-vngine/services"

	"github.com/stretchr/testify/assert"
)

func TestSourceNameConverter(t *testing.T) {

	stringRegistry := services.FIXTURE_StringRegistry()
	t.Run("it converts simple expressions ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			BoolLiteral(false):              "is_false",
			BoolLiteral(true):               "is_true",
			VariableReference("a_bool_var"): "is_VAR_a_bool_var",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts AND and OR expressions ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			And{BoolLiteral(true), BoolLiteral(false)}: "is_true_AND_false",
			Or{BoolLiteral(false), BoolLiteral(true)}:  "is_false_OR_true",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts MORE_THAN and LESS_THAN expressions ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			MoreThan{NumberLiteral(1), NumberLiteral(2)}:                          "is_1_MORE_THAN_2",
			LessThan{VariableReference("my_int"), VariableReference("other_int")}: "is_VAR_my_int_LESS_THAN_VAR_other_int",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts equals operator expressions ", func(t *testing.T) {
		stringRegistry := services.FIXTURE_StringRegistryFrom([]string{"somestring"})
		reference, _ := stringRegistry.Reference("somestring")

		for expression, expectedName := range map[Expression]string{
			Equal{BoolLiteral(true), BoolLiteral(false)}:                        "is_true_EQUALS_false",
			Equal{VariableReference("anInt"), NumberLiteral(1234)}:              "is_VAR_anInt_EQUALS_1234",
			Equal{StringLiteral("somestring"), VariableReference("aStringVar")}: fmt.Sprintf("is_%s_EQUALS_VAR_aStringVar", reference),
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it referes to strings using their registry reference ", func(t *testing.T) {
		stringLiteral := "hello world!"
		stringRegistry := services.FIXTURE_StringRegistryFrom([]string{stringLiteral})
		stringReference, err := stringRegistry.Reference(stringLiteral)
		assert.NoError(t, err)

		for expression, expectedName := range map[Expression]string{
			Equal{VariableReference("aString"), StringLiteral(stringLiteral)}: fmt.Sprintf("is_VAR_aString_EQUALS_%s", stringReference),
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts brackets operator expressions ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			Brackets{Equal{BoolLiteral(true), BoolLiteral(false)}}: "is_BO_true_EQUALS_false_BC",
			And{
				Brackets{Equal{BoolLiteral(true), BoolLiteral(false)}},
				LessThan{VariableReference("someInt"), NumberLiteral(123)},
			}: "is_BO_true_EQUALS_false_BC_AND_VAR_someInt_LESS_THAN_123",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts equals operator expressions ", func(t *testing.T) {
		for expression, expectedName := range map[Expression]string{
			Equal{BoolLiteral(true), BoolLiteral(false)}:           "is_true_EQUALS_false",
			Equal{VariableReference("anInt"), NumberLiteral(1234)}: "is_VAR_anInt_EQUALS_1234",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})
}
