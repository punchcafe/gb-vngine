package validate

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services/expression"
)

func TestTypeResolver(t *testing.T) {
	gameState := project.GameState{"int_var": project.INT, "str_var": project.STRING, "bool_var": project.BOOL}

	// TODO: pad out this test suite
	t.Run("it resolves simple expressions to their returned type", func(t *testing.T) {
		for expression, expectedType := range map[expression.Expression]project.GameStateVariableType{
			expression.Brackets{expression.And{expression.BoolLiteral(false), expression.VariableReference("bool_var")}}: project.BOOL,
			expression.LessThan{expression.VariableReference("int_var"), expression.NumberLiteral(1)}:                    project.BOOL,
			expression.NumberLiteral(1): project.INT,
		} {
			validateTypeCheck(t, gameState, expectedType, expression)
		}
	})

	t.Run("less_than operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []expression.Expression{
			expression.LessThan{expression.BoolLiteral(true), expression.NumberLiteral(1)},
			expression.LessThan{expression.NumberLiteral(1), expression.BoolLiteral(true)},
			expression.LessThan{expression.VariableReference("bool_var"), expression.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an less_than expression must both evaluate to integer", expression)
		}

		// Valid checks
		for _, expression := range []expression.Expression{
			expression.LessThan{expression.NumberLiteral(2), expression.NumberLiteral(1)},
			expression.LessThan{expression.VariableReference("int_var"), expression.NumberLiteral(1)},
			expression.LessThan{expression.VariableReference("int_var"), expression.NumberLiteral(1)},
			expression.LessThan{expression.NumberLiteral(1), expression.VariableReference("int_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("more_than operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []expression.Expression{
			expression.MoreThan{expression.BoolLiteral(true), expression.NumberLiteral(1)},
			expression.MoreThan{expression.NumberLiteral(1), expression.BoolLiteral(true)},
			expression.MoreThan{expression.VariableReference("bool_var"), expression.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an more_than expression must both evaluate to integer", expression)
		}

		// Valid checks
		for _, expression := range []expression.Expression{
			expression.MoreThan{expression.NumberLiteral(2), expression.NumberLiteral(1)},
			expression.MoreThan{expression.VariableReference("int_var"), expression.NumberLiteral(1)},
			expression.MoreThan{expression.NumberLiteral(1), expression.VariableReference("int_var")},
			expression.MoreThan{expression.VariableReference("int_var"), expression.VariableReference("int_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("OR operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []expression.Expression{
			expression.Or{expression.BoolLiteral(true), expression.NumberLiteral(1)},
			expression.Or{expression.NumberLiteral(1), expression.BoolLiteral(true)},
			expression.Or{expression.VariableReference("bool_var"), expression.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an or expression must both evaluate to boolean", expression)
		}

		// Valid checks
		for _, expression := range []expression.Expression{
			expression.Or{expression.BoolLiteral(true), expression.BoolLiteral(false)},
			expression.Or{expression.VariableReference("bool_var"), expression.BoolLiteral(false)},
			expression.Or{expression.BoolLiteral(false), expression.VariableReference("bool_var")},
			expression.Or{expression.VariableReference("bool_var"), expression.VariableReference("bool_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("AND operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []expression.Expression{
			expression.And{expression.BoolLiteral(true), expression.NumberLiteral(1)},
			expression.And{expression.NumberLiteral(1), expression.BoolLiteral(true)},
			expression.And{expression.VariableReference("bool_var"), expression.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an and expression must both evaluate to boolean", expression)
		}

		// Valid checks
		for _, expression := range []expression.Expression{
			expression.And{expression.BoolLiteral(true), expression.BoolLiteral(false)},
			expression.And{expression.VariableReference("bool_var"), expression.BoolLiteral(false)},
			expression.And{expression.BoolLiteral(false), expression.VariableReference("bool_var")},
			expression.And{expression.VariableReference("bool_var"), expression.VariableReference("bool_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

}

func assertError(t *testing.T, gs project.GameState, expectedErrorMessage string, e expression.Expression) {
	tr := newTypeResolver(gs)
	_, err := tr.ResolveType(e)
	assert.Error(t, err)
	assert.Equal(t, err.Error(), expectedErrorMessage)
}

func validateTypeCheck(t *testing.T, gs project.GameState, gsvt project.GameStateVariableType, e expression.Expression) {
	tr := newTypeResolver(gs)
	typ, err := tr.ResolveType(e)
	assert.NoError(t, err)
	assert.Equal(t, typ, gsvt)
}

func newTypeResolver(gs project.GameState) *TypeResolver {
	return &TypeResolver{gs: gs}
}
