package predicate

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
	"punchcafe.dev/gb-vngine/services/predicate/registry"
)

func TestXxx(t *testing.T) {
	t.Run("it renders test fixtures correctly", func(t *testing.T) {
		// TODO: rename these
		// TODO: add more extensive cases
		// TODO: add failure cases
		pr := registry.FIXTURE_PredicateRegistry()
		sr := services.BuildStringRegistry(&pr)
		gs := project.GameState{"my_string": project.STRING, "my_num": project.INT}

		subject := FunctionRenderer{
			ps: &pr,
			gs: &gs,
			sr: sr,
		}

		expected := `
bool is_true_AND_true(struct GameState * game_state) {
	return true && true;
};
bool is_STRING_REG_1_EQUALS_VAR_my_string(struct GameState * game_state) {
	return str_compare(STRING_REG_1, game_state->my_string);
};
bool is_VAR_my_num_EQUALS_1(struct GameState * game_state) {
	return game_state->my_num == 1;
};`
		result, err := subject.Render()
		assert.NoError(t, err)
		assert.Equal(t, expected, result)
	})
}
