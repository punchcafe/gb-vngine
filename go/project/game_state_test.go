package project

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestParseGameState(t *testing.T) {
	t.Run("Can correctly parse string maps", func(t *testing.T) {

		expected := GameState(map[GameStateVariableName]GameStateVariableType{
			"int_field":          INT,
			"other_int_field":    INT,
			"string_field":       STRING,
			"other_string_field": STRING,
			"bool_field":         BOOL,
			"other_bool_filed":   BOOL,
		})

		result, err := ParseGameState(
			map[string]string{
				"int_field":          "INT",
				"other_int_field":    "int",
				"string_field":       "STRING",
				"other_string_field": "String",
				"bool_field":         "BOOL",
				"other_bool_filed":   "boOL",
			},
		)

		assert.NoError(t, err)
		assert.Equal(t, result, expected)
	})

	t.Run("it detects invalid variable types and returns errors", func(t *testing.T) {
		_, err := ParseGameState(
			map[string]string{
				"invalid_field": "INVALID",
			},
		)

		assert.EqualError(t, err, "invalid game state variable type: INVALID")
	})
}
