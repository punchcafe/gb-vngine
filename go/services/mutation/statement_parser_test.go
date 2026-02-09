package mutation

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/services/expression"
)

func TestStatementParserWithAddStatements(t *testing.T) {

	t.Run("It correctly parses statments", func(t *testing.T) {
		result, err := ParseStatement("add($someInt, 2)")
		assert.NoError(t, err)
		assert.Equal(t, AddFunction{variable: "someInt", amount: expression.NumberLiteral(2)}, result)

		result, err = ParseStatement("add($someInt, -2)")
		assert.NoError(t, err)
		assert.Equal(t, AddFunction{variable: "someInt", amount: expression.NumberLiteral(-2)}, result)
	})

	t.Run("It returns an error for statments when the arguments are wrong", func(t *testing.T) {
		// Case when the target variable isn't a variable

		_, err := ParseStatement("add(1, 2)")
		assert.Error(t, err)
		assert.Equal(t, "first argument to statement not a variable", err.Error())

		// Case when the target add value isn't an integer

		// TODO: consider whether this validation belongs here, or whether these functions should be semantically simpler
		// and _all_ validation is passed to the AddFunction SetFunction methods.

		_, err = ParseStatement("add($someInt, true)")
		assert.Error(t, err)
		assert.Equal(t, "Invalid argument in add() statement: second argument must be a valid number", err.Error())
	})

}

func TestStatementParserWithSetStatements(t *testing.T) {

	t.Run("It correctly parses statments", func(t *testing.T) {
		result, err := ParseStatement("set($someVar, 2)")
		assert.NoError(t, err)
		assert.Equal(t, SetFunction{variable: "someVar", newValue: expression.NumberLiteral(2)}, result)
	})

	t.Run("It returns an error when the arguments are wrong", func(t *testing.T) {
		// Case when the target variable isn't a variable

		_, err := ParseStatement("set(1, 2)")
		assert.Error(t, err)
		assert.Equal(t, "first argument to statement not a variable", err.Error())
	})

}
