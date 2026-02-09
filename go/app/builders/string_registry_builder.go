package builders

import (
	s "punchcafe.dev/gb-vngine/services"
	e "punchcafe.dev/gb-vngine/services/expression"
	predicate "punchcafe.dev/gb-vngine/services/predicate/registry"
)

// Expression traversal

// TODO: move to builder layer and keep more generic
func BuildStringRegistry(ps *predicate.Registry) *s.StringRegistry {
	sr := s.NewRegistry()
	v := expressionStringExtractor{sr: &sr}

	for _, e := range ps.AllRegisteredPredicates() {
		e.AcceptVisitor(&v)
	}

	return &sr
}

type expressionStringExtractor struct {
	sr *s.StringRegistry
}

func (ese *expressionStringExtractor) VisitVariableReference(_ e.VariableReference) {}
func (ese *expressionStringExtractor) VisitNumberLiteral(_ e.NumberLiteral)         {}
func (ese *expressionStringExtractor) VisitBoolLiteral(_ e.BoolLiteral)             {}

func (ese *expressionStringExtractor) VisitStringLiteral(e e.StringLiteral) {
	ese.sr.AddString(string(e))
}

func (ese *expressionStringExtractor) VisitBrackets(e e.Brackets) {
	e.InnerExpression.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitMoreThan(e e.MoreThan) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitLessThan(e e.LessThan) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitAnd(e e.And) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitOr(e e.Or) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitEqual(e e.Equal) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}
