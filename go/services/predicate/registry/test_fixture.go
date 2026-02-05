package registry

import "punchcafe.dev/gb-vngine/services/predicate"

func FIXTURE_PredicateRegistry() Registry {
	return Registry{AllPredicates: []predicate.Expression{
		predicate.And{
			Lhs: predicate.BoolLiteral(true),
			Rhs: predicate.BoolLiteral(true),
		},
		predicate.Equal{
			Lhs: predicate.StringLiteral("hello"),
			Rhs: predicate.VariableReference("my_string"),
		},
		predicate.Equal{
			Lhs: predicate.VariableReference("my_num"),
			Rhs: predicate.NumberLiteral(1),
		},
	}}
}

func FIXTURE_PredicateRegistryFrom(predicates []predicate.Expression) Registry {
	return Registry{AllPredicates: predicates}
}
