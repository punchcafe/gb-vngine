package predicate

import (
	"fmt"
	"strings"

	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/render"
	"punchcafe.dev/gb-vngine/services"
	e "punchcafe.dev/gb-vngine/services/expression"
	"punchcafe.dev/gb-vngine/services/predicate/name"
	"punchcafe.dev/gb-vngine/services/predicate/registry"
)

const PREDICATE_FUNCTION_RENDERER_NAME = "PREDICATE_FUNCTION_RENDERER"

// Generates a named predicate function for each distinct predicate expression
// in the graph.

type FunctionRenderer struct {
	ps *registry.Registry
	sr *services.StringRegistry
	// TODO: replace this with game state service
	gs *project.GameState
}

func BuildFunctionRenderer(
	predicateRegistry *registry.Registry,
	stringRegsitry *services.StringRegistry,
	gameState *project.GameState,
) *FunctionRenderer {
	return &FunctionRenderer{ps: predicateRegistry, sr: stringRegsitry, gs: gameState}
}

func (fr *FunctionRenderer) Render() (string, error) {
	builder := strings.Builder{}
	for _, e := range fr.ps.AllRegisteredPredicates() {
		functionDefinition, err := fr.renderFunction(e)
		if err != nil {
			return "", err
		}

		builder.WriteString("\n")
		builder.WriteString(functionDefinition)
	}

	return builder.String(), nil
}

func (fr *FunctionRenderer) Name() string {
	return PREDICATE_FUNCTION_RENDERER_NAME
}

func (fr *FunctionRenderer) Dependencies() []string {
	return []string{render.TYPE_DEFINITION_RENDERER_NAME, render.STRING_CONSTANTS_RENDERER_NAME}
}

func (fr *FunctionRenderer) renderFunction(e e.Expression) (string, error) {
	name, err := name.ExpressionToSourceName(e, fr.sr)
	if err != nil {
		return "", err
	}
	body, err := ConvertExpressionToSource(e, *fr.gs, fr.sr)
	if err != nil {
		return "", err
	}

	functionDef := fmt.Sprintf(`bool %s(struct GameState * game_state) {
	return %s;
}`, name, body)
	return functionDef, nil
}
