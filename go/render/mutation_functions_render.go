package render

import (
	"fmt"
	"strings"

	"punchcafe.dev/gb-vngine/services/mutation"
)

const MUTATION_FUNCTION_RENDERER_NAME = "MUTATION_FUNCTION_RENDERER_NAME"

type MutationFunctionsRenderer struct {
	mutationRegistry *mutation.Registry
	mutationService  *mutation.Service
}

func BuildMutationFunctionsRenderer(r *mutation.Registry, s *mutation.Service) *MutationFunctionsRenderer {
	return &MutationFunctionsRenderer{mutationRegistry: r, mutationService: s}
}

func (m *MutationFunctionsRenderer) Name() string {
	return MUTATION_FUNCTION_RENDERER_NAME
}

func (m *MutationFunctionsRenderer) Dependencies() []string {
	return []string{GAME_STATE_RENDERER_NAME, TYPE_DEFINITION_RENDERER_NAME, STRING_CONSTANTS_RENDERER_NAME}
}

func (m *MutationFunctionsRenderer) Render() (string, error) {
	b := strings.Builder{}
	for _, statement := range m.mutationRegistry.AllMutationStatements() {
		identifier, err := m.mutationService.StatementIdentifier(statement)
		if err != nil {
			return "", err
		}
		sourceCode, err := m.mutationService.StatementSourceCode(statement)
		if err != nil {
			return "", err
		}
		b.WriteString(fmt.Sprintf("\nvoid %s(struct GameState * game_state){\n  %s\n}", identifier, sourceCode))
	}
	return b.String(), nil
}
