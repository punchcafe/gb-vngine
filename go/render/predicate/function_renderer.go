package predicate

import (
	"fmt"
	"strings"

	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
	"punchcafe.dev/gb-vngine/services/predicate"
	"punchcafe.dev/gb-vngine/services/predicate/name"
	"punchcafe.dev/gb-vngine/services/predicate/registry"
)

// Generates a named predicate function for each distinct predicate expression
// in the graph.

type FunctionRenderer struct {
	ps *registry.Registry
	sr *services.StringRegistry
	gs *project.GameState
}

func (fr *FunctionRenderer) Render() (string, error) {
	builder := strings.Builder{}
	for e := range fr.ps.AllRegisteredPredicates() {
		functionDefinition, err := fr.renderFunction(e)
		if err != nil {
			return "", err
		}

		builder.WriteString("\n")
		builder.WriteString(functionDefinition)
	}

	return builder.String(), nil
}

func (fr *FunctionRenderer) renderFunction(e predicate.Expression) (string, error) {
	name, err := name.ExpressionToSourceName(e)
	if err != nil {
		return "", err
	}
	body, err := ConvertExpressionToSource(e, *fr.gs, fr.sr)
	if err != nil {
		return "", err
	}

	functionDef := fmt.Sprintf(`bool %s {
	return %s;
};`, name, body)
	return functionDef, nil
}
