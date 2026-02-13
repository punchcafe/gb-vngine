package mutation

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
	"punchcafe.dev/gb-vngine/services/expression"
)

func TestServiceStatementIdentifier(t *testing.T) {
	t.Run("It can convert set mutations", func(t *testing.T) {
		sr := services.FIXTURE_StringRegistryFrom([]string{"sample_string_a", "sample_string_b"})
		gs := project.GameState(map[project.GameStateVariableName]project.GameStateVariableType{
			"some_int":    project.INT,
			"some_string": project.STRING,
			"some_bool":   project.BOOL,
		})
		es := expression.BuildService(gs, &sr)
		subject := &Service{expressionService: es}
		for input, expected := range map[string]string{
			"set($some_string, \"sample_string_a\")":                     "MUTATION_SET_is_VAR_some_string_is_STRING_REG_1",
			"set($some_string, \"sample_string_b\")":                     "MUTATION_SET_is_VAR_some_string_is_STRING_REG_2",
			"set($some_int, 5)":                                          "MUTATION_SET_is_VAR_some_int_is_5",
			"set($some_bool, true)":                                      "MUTATION_SET_is_VAR_some_bool_is_true",
			"set($some_bool, false)":                                     "MUTATION_SET_is_VAR_some_bool_is_false",
			"set($some_bool, ($some_counter less_than 3) or $some_bool)": "MUTATION_SET_is_VAR_some_bool_is_BO_VAR_some_counter_LESS_THAN_3_BC_OR_VAR_some_bool",
		} {
			statement, err := ParseStatement(input)
			assert.NoError(t, err)
			identifier, err := subject.StatementIdentifier(statement)
			assert.NoError(t, err)
			// TODO: update expression service to not include is_ prefix and extract to predicate service
			assert.Equal(t, expected, identifier)
		}

	})

	t.Run("It can convert add mutations", func(t *testing.T) {
		sr := services.FIXTURE_StringRegistryFrom([]string{})
		gs := project.GameState(map[project.GameStateVariableName]project.GameStateVariableType{
			"some_int": project.INT,
		})
		es := expression.BuildService(gs, &sr)
		subject := &Service{expressionService: es}
		for input, expected := range map[string]string{
			"add($some_int, 5)": "MUTATION_ADD_is_VAR_some_int_5",
			// TODO: fix this so negative numbers can be correctly rendered
			"add($some_int, -5)": "MUTATION_ADD_is_VAR_some_int_-5",
		} {
			statement, err := ParseStatement(input)
			assert.NoError(t, err)
			identifier, err := subject.StatementIdentifier(statement)
			assert.NoError(t, err)
			// TODO: update expression service to not include is_ prefix and extract to predicate service
			assert.Equal(t, expected, identifier)
		}

		// TODO: add invalid cases
	})

}
