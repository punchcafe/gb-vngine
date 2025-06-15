package services

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
)

var gameState, _ = project.ParseGameState(
	map[string]string{
		"int_field":          "INT",
		"other_int_field":    "int",
		"string-field":       "STRING",
		"other-string_field": "String",
		"bool-field":         "BOOL",
		"other_bool_filed":   "boOL",
	},
)

var service, _ = NewGameStateService(gameState)

func TestLookupFieldType(t *testing.T) {
	t.Run("returns the expected c types", func(t *testing.T) {
		for fieldName, expectedType := range map[project.GameStateVariableName]SourceVariableType{
			"int_field":    "int",
			"string-field": "char *",
			"bool-field":   "unsigned char",
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
		result, err := service.LookupFieldName("other-string_field")
		assert.NoError(t, err)
		assert.Equal(t, SourceVariableName("other_string_field"), result)
	})
}

func TestAllVariables(t *testing.T) {
	t.Run("Returns all variables in a consistent order", func(t *testing.T) {

		assert.Equal(t, []project.GameStateVariableName{
			"bool-field",
			"int_field",
			"other-string_field",
			"other_bool_filed",
			"other_int_field",
			"string-field"},
			service.AllVariables())
	})
}
