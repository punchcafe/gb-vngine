package registry

import "punchcafe.dev/gb-vngine/services/predicate"

func FIXTURE_PredicateRegistry() Registry {
	return Registry{AllPredicates: map[predicate.Expression]bool{
		predicate.And{
			Lhs: predicate.BoolLiteral(true),
			Rhs: predicate.BoolLiteral(true),
		}: true,
		predicate.Equal{
			Lhs: predicate.StringLiteral("hello"),
			Rhs: predicate.VariableReference("my_string"),
		}: true,
		predicate.Equal{
			Lhs: predicate.VariableReference("my_num"),
			Rhs: predicate.NumberLiteral(1),
		}: true,
	}}
}
