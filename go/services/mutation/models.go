package mutation

import (
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services/predicate"
)

// TODO: need to fix this dependency cycle by extracting common syntax into its own
// module.
// Maybe we should consider decoupling expressions and predicate expressions, having a predicate expression be something which evaluates to a bool.
// Extract expression to syntaxt module

type SetFunction struct {
	variable predicate.VariableReference
	newValue predicate.Expression
}

type AddFunction struct {
	variable predicate.VariableReference
	amount   predicate.NumberLiteral
}

type MutationStatement interface {
	Validate(gameState project.GameState) error
}

// TODO: implement these properly, but now use as a type definition.
func (af *AddFunction) Validate(_ project.GameState) error {
	return nil
}

func (af *SetFunction) Validate(_ project.GameState) error {
	return nil
}
