package services

import (
	"fmt"
	"slices"

	p "punchcafe.dev/gb-vngine/services/predicate"
	"punchcafe.dev/gb-vngine/services/predicate/registry"
)

type StringRegistry struct {
	// TODO: improve this to use a better insertion-ordered set
	// Alteratively, can order by string constant when retrieving all
	entries []StringEntry
}

func BuildStringRegistry(ps *registry.Registry) *StringRegistry {
	sr := NewRegistry()
	v := expressionStringExtractor{sr: &sr}

	for _, e := range ps.AllRegisteredPredicates() {
		e.AcceptVisitor(&v)
	}

	return &sr
}

func (sr *StringRegistry) Reference(value string) (string, error) {
	index := slices.IndexFunc(sr.entries, func(se StringEntry) bool {
		return se.Value == value
	})

	if index == -1 {
		return "", fmt.Errorf("string value not registered in string registry")
	}
	return sr.entries[index].ConstantName, nil
}

type StringEntry struct {
	ConstantName string
	Value        string
}

func (st *StringRegistry) AllStrings() []StringEntry {
	return st.entries
}

func constantName(index uint) string {
	return fmt.Sprintf("STRING_REG_%d", index)
}

func NewRegistry() StringRegistry {
	return StringRegistry{make([]StringEntry, 0)}
}

// Idempotent
func (sr *StringRegistry) AddString(value string) {
	index := slices.IndexFunc(sr.entries, func(se StringEntry) bool {
		return se.Value == value
	})

	if index != -1 {
		// already added
		return
	}
	entryIndex := uint(len(sr.entries) + 1)
	sr.entries = append(sr.entries, StringEntry{constantName(entryIndex), value})
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
