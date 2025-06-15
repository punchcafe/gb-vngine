package render

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
)

func TestRender(t *testing.T) {
	t.Run("Can correctly render Game State", func(t *testing.T) {

		gameState, _ := project.ParseGameState(
			map[string]string{
				"int_field":          "INT",
				"other_int_field":    "int",
				"string_field":       "STRING",
				"other_string_field": "String",
				"bool_field":         "BOOL",
				"other_bool_filed":   "boOL",
			},
		)

		service, _ := services.NewGameStateService(gameState)

		renderer := NewGameStateRenderer(service)
		result, err := renderer.Render()

		expected := `
struct GameState {
    unsigned char bool_field;
    int int_field;
    unsigned char other_bool_filed;
    int other_int_field;
    char * other_string_field;
    char * string_field;

};
`
		assert.NoError(t, err)
		assert.Equal(t, expected, result)
	})
}
