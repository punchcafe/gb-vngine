package mutation

import (
	"punchcafe.dev/gb-vngine/project"
	e "punchcafe.dev/gb-vngine/services/expression"
)

// TODO: need to fix this dependency cycle by extracting common syntax into its own
// module.
// Maybe we should consider decoupling expressions and predicate expressions, having a predicate expression be something which evaluates to a bool.
// Extract expression to syntaxt module

type SetFunction struct {
	variable e.VariableReference
	newValue e.Expression
}

type AddFunction struct {
	variable e.VariableReference
	amount   e.NumberLiteral
}

type MutationStatement interface {
	Validate(project.GameState) error
}

func (af SetFunction) Expression() e.Expression {
	return af.newValue
}

// TODO: implement these properly, but now use as a type definition.
func (af AddFunction) Validate(_ project.GameState) error {
	return nil
}

func (af SetFunction) Validate(_ project.GameState) error {
	return nil
}
