package mutation

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
)

func Test_registerRawStatement(t *testing.T) {

	t.Run("It adds a statement to the registry", func(t *testing.T) {
		// Arrange
		statmentString := "set($some_bool, true)"
		gameState := project.GameState{"some_bool": project.BOOL}
		statementModel, _ := ParseStatement(statmentString)
		registry := newRegistry(gameState)

		// Act
		err := registry.registerRawStatement(statmentString)

		assert.NoError(t, err)
		assert.Equal(t, []MutationStatement{statementModel}, registry.statements)
	})

	t.Run("It adds multiple statements to the registry", func(t *testing.T) {
		// Arrange
		statmentString := "set($some_bool, true)"
		otherStatmentString := "add($some_int, -1)"
		gameState := project.GameState{"some_bool": project.BOOL, "some_int": project.INT}
		statementModel, _ := ParseStatement(statmentString)
		otherStatementModel, _ := ParseStatement(otherStatmentString)
		registry := newRegistry(gameState)

		// Act
		errOne := registry.registerRawStatement(statmentString)
		errTwo := registry.registerRawStatement(otherStatmentString)

		// Assert
		assert.NoError(t, errOne)
		assert.NoError(t, errTwo)
		assert.Equal(t, []MutationStatement{statementModel, otherStatementModel}, registry.statements)
	})

	t.Run("It ignores a statement if it's already added", func(t *testing.T) {
		// Arrange
		statmentString := "set($some_bool, true)"
		gameState := project.GameState{"some_bool": project.BOOL}
		statementModel, _ := ParseStatement(statmentString)
		registry := newRegistry(gameState)

		// Act
		errOne := registry.registerRawStatement(statmentString)
		errTwo := registry.registerRawStatement(statmentString)

		assert.NoError(t, errOne)
		assert.NoError(t, errTwo)
		assert.Equal(t, []MutationStatement{statementModel}, registry.statements)
	})

	t.Run("It returns an error if the statement is invalid", func(t *testing.T) {
		gameState := project.GameState{"some_bool": project.BOOL}
		for invalidStatement, expectedError := range map[string]string{
			"invalid structure":     "invalid statement syntax: invalid structure",
			"add($some_bool, true)": "Invalid argument in add() statement: second argument must be a valid number",
			// TODO: add checks for invalid variable type
			// TODO add checks for invalid expression type
		} {
			// Arrange
			registry := newRegistry(gameState)

			// Act
			err := registry.registerRawStatement(invalidStatement)

			assert.Error(t, err)
			assert.Equal(t, expectedError, err.Error())
		}

	})
}

func newRegistry(gameState project.GameState) Registry {
	return Registry{statements: []MutationStatement{}, gameState: gameState}
}
