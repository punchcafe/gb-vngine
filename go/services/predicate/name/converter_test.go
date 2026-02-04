package name

import (
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/services"
	"punchcafe.dev/gb-vngine/services/predicate"
)

func TestSourceNameConverter(t *testing.T) {

	stringRegistry := services.FIXTURE_StringRegistry()
	t.Run("it converts simple expressions ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.BoolLiteral(false):              "is_false",
			predicate.BoolLiteral(true):               "is_true",
			predicate.VariableReference("a_bool_var"): "is_VAR_a_bool_var",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts AND and OR expressions ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.And{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}: "is_true_AND_false",
			predicate.Or{predicate.BoolLiteral(false), predicate.BoolLiteral(true)}:  "is_false_OR_true",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts MORE_THAN and LESS_THAN expressions ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.MoreThan{predicate.NumberLiteral(1), predicate.NumberLiteral(2)}:                          "is_1_MORE_THAN_2",
			predicate.LessThan{predicate.VariableReference("my_int"), predicate.VariableReference("other_int")}: "is_VAR_my_int_LESS_THAN_VAR_other_int",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts equals operator expressions ", func(t *testing.T) {
		stringRegistry := services.FIXTURE_StringRegistryFrom([]string{"somestring"})
		reference, _ := stringRegistry.Reference("somestring")

		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Equal{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}:                        "is_true_EQUALS_false",
			predicate.Equal{predicate.VariableReference("anInt"), predicate.NumberLiteral(1234)}:              "is_VAR_anInt_EQUALS_1234",
			predicate.Equal{predicate.StringLiteral("somestring"), predicate.VariableReference("aStringVar")}: fmt.Sprintf("is_%s_EQUALS_VAR_aStringVar", reference),
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

		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Equal{predicate.VariableReference("aString"), predicate.StringLiteral(stringLiteral)}: fmt.Sprintf("is_VAR_aString_EQUALS_%s", stringReference),
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts brackets operator expressions ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Brackets{predicate.Equal{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}}: "is_BO_true_EQUALS_false_BC",
			predicate.And{
				predicate.Brackets{predicate.Equal{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}},
				predicate.LessThan{predicate.VariableReference("someInt"), predicate.NumberLiteral(123)},
			}: "is_BO_true_EQUALS_false_BC_AND_VAR_someInt_LESS_THAN_123",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})

	t.Run("it converts equals operator expressions ", func(t *testing.T) {
		for expression, expectedName := range map[predicate.Expression]string{
			predicate.Equal{predicate.BoolLiteral(true), predicate.BoolLiteral(false)}:           "is_true_EQUALS_false",
			predicate.Equal{predicate.VariableReference("anInt"), predicate.NumberLiteral(1234)}: "is_VAR_anInt_EQUALS_1234",
		} {
			res, err := ExpressionToSourceName(expression, &stringRegistry)
			assert.NoError(t, err)
			assert.Equal(t, expectedName, res)
		}
	})
}
