package registry

import "punchcafe.dev/gb-vngine/services/expression"

func FIXTURE_PredicateRegistry() Registry {
	return Registry{AllPredicates: []expression.Expression{
		expression.And{
			Lhs: expression.BoolLiteral(true),
			Rhs: expression.BoolLiteral(true),
		},
		expression.Equal{
			Lhs: expression.StringLiteral("hello"),
			Rhs: expression.VariableReference("my_string"),
		},
		expression.Equal{
			Lhs: expression.VariableReference("my_num"),
			Rhs: expression.NumberLiteral(1),
		},
	}}
}

func FIXTURE_PredicateRegistryFrom(predicates []expression.Expression) Registry {
	return Registry{AllPredicates: predicates}
}
