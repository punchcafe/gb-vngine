package expression

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
)

func Test_typeResolver(t *testing.T) {
	gameState := project.GameState{"int_var": project.INT, "str_var": project.STRING, "bool_var": project.BOOL}

	// TODO: pad out this test suite
	t.Run("it resolves simple expressions to their returned type", func(t *testing.T) {
		for expression, expectedType := range map[Expression]project.GameStateVariableType{
			Brackets{And{BoolLiteral(false), VariableReference("bool_var")}}: project.BOOL,
			LessThan{VariableReference("int_var"), NumberLiteral(1)}:         project.BOOL,
			NumberLiteral(1): project.INT,
		} {
			validateTypeCheck(t, gameState, expectedType, expression)
		}
	})

	t.Run("less_than operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []Expression{
			LessThan{BoolLiteral(true), NumberLiteral(1)},
			LessThan{NumberLiteral(1), BoolLiteral(true)},
			LessThan{VariableReference("bool_var"), NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an less_than expression must both evaluate to integer", expression)
		}

		// Valid checks
		for _, expression := range []Expression{
			LessThan{NumberLiteral(2), NumberLiteral(1)},
			LessThan{VariableReference("int_var"), NumberLiteral(1)},
			LessThan{VariableReference("int_var"), NumberLiteral(1)},
			LessThan{NumberLiteral(1), VariableReference("int_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("more_than operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []Expression{
			MoreThan{BoolLiteral(true), NumberLiteral(1)},
			MoreThan{NumberLiteral(1), BoolLiteral(true)},
			MoreThan{VariableReference("bool_var"), NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an more_than expression must both evaluate to integer", expression)
		}

		// Valid checks
		for _, expression := range []Expression{
			MoreThan{NumberLiteral(2), NumberLiteral(1)},
			MoreThan{VariableReference("int_var"), NumberLiteral(1)},
			MoreThan{NumberLiteral(1), VariableReference("int_var")},
			MoreThan{VariableReference("int_var"), VariableReference("int_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("OR operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []Expression{
			Or{BoolLiteral(true), NumberLiteral(1)},
			Or{NumberLiteral(1), BoolLiteral(true)},
			Or{VariableReference("bool_var"), NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an or expression must both evaluate to boolean", expression)
		}

		// Valid checks
		for _, expression := range []Expression{
			Or{BoolLiteral(true), BoolLiteral(false)},
			Or{VariableReference("bool_var"), BoolLiteral(false)},
			Or{BoolLiteral(false), VariableReference("bool_var")},
			Or{VariableReference("bool_var"), VariableReference("bool_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("AND operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []Expression{
			And{BoolLiteral(true), NumberLiteral(1)},
			And{NumberLiteral(1), BoolLiteral(true)},
			And{VariableReference("bool_var"), NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an and expression must both evaluate to boolean", expression)
		}

		// Valid checks
		for _, expression := range []Expression{
			And{BoolLiteral(true), BoolLiteral(false)},
			And{VariableReference("bool_var"), BoolLiteral(false)},
			And{BoolLiteral(false), VariableReference("bool_var")},
			And{VariableReference("bool_var"), VariableReference("bool_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

}

func assertError(t *testing.T, gs project.GameState, expectedErrorMessage string, e Expression) {
	tr := newtypeResolver(gs)
	_, err := tr.resolveType(e)
	assert.Error(t, err)
	assert.Equal(t, err.Error(), expectedErrorMessage)
}

func validateTypeCheck(t *testing.T, gs project.GameState, gsvt project.GameStateVariableType, e Expression) {
	tr := newtypeResolver(gs)
	typ, err := tr.resolveType(e)
	assert.NoError(t, err)
	assert.Equal(t, typ, gsvt)
}

func newtypeResolver(gs project.GameState) *typeResolver {
	return &typeResolver{gs: gs}
}
