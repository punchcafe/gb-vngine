package services

import (
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
)

var gameState, _ = project.ParseGameState(
	map[string]string{
		"int_field":          "INT",
		"other_int_field":    "int",
		"string_field":       "STRING",
		"other_string_field": "String",
		"bool_field":         "BOOL",
		"other_bool_field":   "boOL",
	},
)

var service, _ = NewGameStateService(gameState)

func TestServiceCreation(t *testing.T) {
	t.Run("it returns an error if game state variable contains an invalid field name", func(t *testing.T) {
		for _, fieldName := range []string{
			"int-field",
			"1int_field",
			"intField@!",
		} {
			gs, _ := project.ParseGameState(map[string]string{fieldName: "INT"})
			expectedMessage := fmt.Sprintf("invalid game state variable name: '%s', must follow the pattern: r/^[a-z_][A-Za-z_]*$/", fieldName)

			gss, err := NewGameStateService(gs)

			assert.EqualError(t, err, expectedMessage)
			assert.Nil(t, gss)
		}
	})
}

func TestLookupFieldType(t *testing.T) {
	t.Run("returns the expected c types", func(t *testing.T) {
		for fieldName, expectedType := range map[project.GameStateVariableName]SourceVariableType{
			"int_field":    "int",
			"string_field": "char *",
			"bool_field":   "unsigned char",
		} {
			result, err := service.LookupFieldType(fieldName)
			assert.NoError(t, err)
			assert.Equal(t, expectedType, result)
		}
	})

	t.Run("returns an error if field doesn't exist", func(t *testing.T) {
		_, err := service.LookupFieldType("unknown_field")
		assert.Error(t, err)
		assert.Equal(t, "no type found for variable: unknown_field", err.Error())
	})
}

func TestLookupFieldName(t *testing.T) {
	t.Run("returns an error if field not in game state", func(t *testing.T) {
		_, err := service.LookupFieldName("unknown_field")
		assert.Error(t, err)
		assert.Equal(t, "unknown game state variable: unknown_field", err.Error())
	})

	t.Run("normalizes name for c code", func(t *testing.T) {
		result, err := service.LookupFieldName("other_string_field")
		assert.NoError(t, err)
		assert.Equal(t, SourceVariableName("other_string_field"), result)
	})
}

func TestAllVariables(t *testing.T) {
	t.Run("Returns all variables in a consistent order", func(t *testing.T) {

		assert.Equal(t, []project.GameStateVariableName{
			"bool_field",
			"int_field",
			"other_bool_field",
			"other_int_field",
			"other_string_field",
			"string_field"},
			service.AllVariables())
	})
}
