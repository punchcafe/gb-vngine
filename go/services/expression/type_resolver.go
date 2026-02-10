package expression

import (
	"fmt"

	"punchcafe.dev/gb-vngine/project"
)

func NewtypeResolver(gs project.GameState) *typeResolver {
	return &typeResolver{gs}
}

type typeResolver struct {
	gs project.GameState
}

// TODO: consider naming here, as well as whether to separate resolving from validating.
func (tr *typeResolver) resolveType(e Expression) (project.GameStateVariableType, error) {
	resolver := typeResolverVisitor{gs: tr.gs}
	e.AcceptVisitor(&resolver)
	if resolver.lastError != nil {
		return project.UNDEFINED, resolver.lastError
	}
	return resolver.lastType, nil
}

// Private structs and functions

type typeResolverVisitor struct {
	gs        project.GameState
	lastType  project.GameStateVariableType
	lastError error
}

func (tr *typeResolverVisitor) VisitVariableReference(vr VariableReference) {
	lookedUpType := tr.gs[project.GameStateVariableName(vr)]
	if lookedUpType != project.UNDEFINED {
		tr.lastType = lookedUpType
		return
	}
	tr.lastError = fmt.Errorf("undefined game state variable referenced: %v", vr)
}

func (tr *typeResolverVisitor) VisitNumberLiteral(NumberLiteral) {
	tr.lastType = project.INT
	tr.lastError = nil
}

func (tr *typeResolverVisitor) VisitBoolLiteral(BoolLiteral) {
	tr.lastType = project.BOOL
	tr.lastError = nil
}

func (tr *typeResolverVisitor) VisitStringLiteral(StringLiteral) {
	tr.lastType = project.STRING
	tr.lastError = nil
}

func (tr *typeResolverVisitor) VisitBrackets(b Brackets) {
	newResolver := typeResolverVisitor{gs: tr.gs}
	b.InnerExpression.AcceptVisitor(&newResolver)
	tr.lastType = newResolver.lastType
	tr.lastError = newResolver.lastError
}

func (tr *typeResolverVisitor) VisitAnd(a And) {
	tr.validateMatchingPair(a.Lhs, a.Rhs, "and", project.BOOL)
}

func (tr *typeResolverVisitor) VisitOr(o Or) {
	tr.validateMatchingPair(o.Lhs, o.Rhs, "or", project.BOOL)
}

func (tr *typeResolverVisitor) VisitEqual(e Equal) {
	// incorrect implementation
	tr.validateMatchingPair(e.Lhs, e.Rhs, "equal", project.STRING)
}

func (tr *typeResolverVisitor) VisitLessThan(lt LessThan) {
	tr.validateMatchingPair(lt.Lhs, lt.Rhs, "less_than", project.INT)
}

func (tr *typeResolverVisitor) VisitMoreThan(mt MoreThan) {
	tr.validateMatchingPair(mt.Lhs, mt.Rhs, "more_than", project.INT)
}

func (tr *typeResolverVisitor) validateMatchingPair(lhs Expression,
	rhs Expression,
	operand string,
	varType project.GameStateVariableType) {

	lhsResolver := typeResolverVisitor{gs: tr.gs}
	rhsResolver := typeResolverVisitor{gs: tr.gs}
	lhs.AcceptVisitor(&lhsResolver)
	rhs.AcceptVisitor(&rhsResolver)

	if lhsResolver.lastError != nil {
		tr.lastError = lhsResolver.lastError
		return
	}

	if rhsResolver.lastError != nil {
		tr.lastError = rhsResolver.lastError
		return
	}

	if lhsResolver.lastType == varType && rhsResolver.lastType == varType {
		tr.lastError = nil
		tr.lastType = project.BOOL
		return
	}

	tr.lastError = fmt.Errorf("expressions in an %v expression must both evaluate to %v", operand, typeName(varType))
}

func typeName(t project.GameStateVariableType) string {
	switch t {
	case project.BOOL:
		return "boolean"
	case project.STRING:
		return "string"
	case project.INT:
		return "integer"
	}
	panic("unexpected type in name lookup")
}
