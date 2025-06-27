package validate

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services/predicate"
)

func TestTypeResolver(t *testing.T) {
	gameState := project.GameState{"int_var": project.INT, "str_var": project.STRING, "bool_var": project.BOOL}

	// TODO: pad out this test suite
	t.Run("it resolves simple expressions to their returned type", func(t *testing.T) {
		for expression, expectedType := range map[predicate.Expression]project.GameStateVariableType{
			predicate.Brackets{predicate.And{predicate.BoolLiteral(false), predicate.VariableReference("bool_var")}}: project.BOOL,
			predicate.LessThan{predicate.VariableReference("int_var"), predicate.NumberLiteral(1)}:                   project.BOOL,
			predicate.NumberLiteral(1): project.INT,
		} {
			validateTypeCheck(t, gameState, expectedType, expression)
		}
	})

	t.Run("less_than operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []predicate.Expression{
			predicate.LessThan{predicate.BoolLiteral(true), predicate.NumberLiteral(1)},
			predicate.LessThan{predicate.NumberLiteral(1), predicate.BoolLiteral(true)},
			predicate.LessThan{predicate.VariableReference("bool_var"), predicate.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an less_than expression must both evaluate to integer", expression)
		}

		// Valid checks
		for _, expression := range []predicate.Expression{
			predicate.LessThan{predicate.NumberLiteral(2), predicate.NumberLiteral(1)},
			predicate.LessThan{predicate.VariableReference("int_var"), predicate.NumberLiteral(1)},
			predicate.LessThan{predicate.VariableReference("int_var"), predicate.NumberLiteral(1)},
			predicate.LessThan{predicate.NumberLiteral(1), predicate.VariableReference("int_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("more_than operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []predicate.Expression{
			predicate.MoreThan{predicate.BoolLiteral(true), predicate.NumberLiteral(1)},
			predicate.MoreThan{predicate.NumberLiteral(1), predicate.BoolLiteral(true)},
			predicate.MoreThan{predicate.VariableReference("bool_var"), predicate.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an more_than expression must both evaluate to integer", expression)
		}

		// Valid checks
		for _, expression := range []predicate.Expression{
			predicate.MoreThan{predicate.NumberLiteral(2), predicate.NumberLiteral(1)},
			predicate.MoreThan{predicate.VariableReference("int_var"), predicate.NumberLiteral(1)},
			predicate.MoreThan{predicate.NumberLiteral(1), predicate.VariableReference("int_var")},
			predicate.MoreThan{predicate.VariableReference("int_var"), predicate.VariableReference("int_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("OR operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []predicate.Expression{
			predicate.Or{predicate.BoolLiteral(true), predicate.NumberLiteral(1)},
			predicate.Or{predicate.NumberLiteral(1), predicate.BoolLiteral(true)},
			predicate.Or{predicate.VariableReference("bool_var"), predicate.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an or expression must both evaluate to boolean", expression)
		}

		// Valid checks
		for _, expression := range []predicate.Expression{
			predicate.Or{predicate.BoolLiteral(true), predicate.BoolLiteral(false)},
			predicate.Or{predicate.VariableReference("bool_var"), predicate.BoolLiteral(false)},
			predicate.Or{predicate.BoolLiteral(false), predicate.VariableReference("bool_var")},
			predicate.Or{predicate.VariableReference("bool_var"), predicate.VariableReference("bool_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

	t.Run("AND operator requires two integers to be valid", func(t *testing.T) {

		// Invalid checks
		for _, expression := range []predicate.Expression{
			predicate.And{predicate.BoolLiteral(true), predicate.NumberLiteral(1)},
			predicate.And{predicate.NumberLiteral(1), predicate.BoolLiteral(true)},
			predicate.And{predicate.VariableReference("bool_var"), predicate.NumberLiteral(1)},
		} {
			assertError(t, gameState, "expressions in an and expression must both evaluate to boolean", expression)
		}

		// Valid checks
		for _, expression := range []predicate.Expression{
			predicate.And{predicate.BoolLiteral(true), predicate.BoolLiteral(false)},
			predicate.And{predicate.VariableReference("bool_var"), predicate.BoolLiteral(false)},
			predicate.And{predicate.BoolLiteral(false), predicate.VariableReference("bool_var")},
			predicate.And{predicate.VariableReference("bool_var"), predicate.VariableReference("bool_var")},
		} {
			validateTypeCheck(t, gameState, project.BOOL, expression)
		}
	})

}

func assertError(t *testing.T, gs project.GameState, expectedErrorMessage string, e predicate.Expression) {
	tr := newTypeResolver(gs)
	e.AcceptVisitor(tr)
	assert.Error(t, tr.lastError)
	assert.Equal(t, tr.lastError.Error(), expectedErrorMessage)
}

func validateTypeCheck(t *testing.T, gs project.GameState, gsvt project.GameStateVariableType, e predicate.Expression) {
	tr := newTypeResolver(gs)
	e.AcceptVisitor(tr)
	assert.NoError(t, tr.lastError)
	assert.Equal(t, tr.lastType, gsvt)
}

func newTypeResolver(gs project.GameState) *TypeResolver {
	return &TypeResolver{gs: gs}
}
