package services

import (
	"fmt"

	p "punchcafe.dev/gb-vngine/services/predicate"
	"punchcafe.dev/gb-vngine/services/predicate/registry"
)

type StringRegistry struct {
	// treat 0 uint as nil
	registry map[string]uint
}

func BuildStringRegistry(ps *registry.Registry) *StringRegistry {
	sr := NewRegistry()
	v := expressionStringExtractor{sr: &sr}

	for e := range ps.AllRegisteredPredicates() {
		e.AcceptVisitor(&v)
	}

	return &sr
}

func (sr *StringRegistry) Reference(value string) (string, error) {
	lookup := sr.registry[value]
	if lookup == 0 {
		return "", fmt.Errorf("string value not registered in string registry")
	}
	return constantName(sr.registry[value]), nil
}

type StringEntry struct {
	ConstantName string
	Value        string
}

func (st *StringRegistry) AllStrings() []StringEntry {
	res := make([]StringEntry, 0)
	for k, v := range st.registry {
		res = append(res, StringEntry{constantName(v), k})
	}
	return res
}

func constantName(index uint) string {
	return fmt.Sprintf("STRING_REG_%d", index)
}

func NewRegistry() StringRegistry {
	return StringRegistry{make(map[string]uint)}
}

// Idempotent
func (sr *StringRegistry) AddString(value string) {
	lookup := sr.registry[value]
	if lookup != 0 {
		// already added
		return
	}
	sr.registry[value] = uint(len(sr.registry) + 1)
}

// Expression traversal

type expressionStringExtractor struct {
	sr *StringRegistry
}

func (ese *expressionStringExtractor) VisitVariableReference(_ p.VariableReference) {}
func (ese *expressionStringExtractor) VisitNumberLiteral(_ p.NumberLiteral)         {}
func (ese *expressionStringExtractor) VisitBoolLiteral(_ p.BoolLiteral)             {}

func (ese *expressionStringExtractor) VisitStringLiteral(e p.StringLiteral) {
	ese.sr.AddString(string(e))
}

func (ese *expressionStringExtractor) VisitBrackets(e p.Brackets) {
	e.InnerExpression.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitMoreThan(e p.MoreThan) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitLessThan(e p.LessThan) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitAnd(e p.And) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitOr(e p.Or) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}

func (ese *expressionStringExtractor) VisitEqual(e p.Equal) {
	e.Lhs.AcceptVisitor(ese)
	e.Rhs.AcceptVisitor(ese)
}
