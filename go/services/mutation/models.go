package mutations

import "punchcafe.dev/gb-vngine/services/predicate"

// TODO: need to fix this dependency cycle by extracting common syntax into its own
// module.

type SetFunction struct {
	variable predicate.VariableReference
	newValue any
}

type AddFunction struct {
	variable predicate.VariableReference
	newValue predicate.NumberLiteral
}
