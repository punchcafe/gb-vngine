package validate

import (
	"fmt"

	"punchcafe.dev/gb-vngine/project"
	p "punchcafe.dev/gb-vngine/services/predicate"
)

type TypeResolver struct {
	gs        project.GameState
	lastType  project.GameStateVariableType
	lastError error
}

func (tr *TypeResolver) VisitVariableReference(vr p.VariableReference) {
	lookedUpType := tr.gs[project.GameStateVariableName(vr)]
	if lookedUpType != project.UNDEFINED {
		tr.lastType = lookedUpType
		return
	}
	tr.lastError = fmt.Errorf("undefined game state variable referenced: %v", vr)
}

func (tr *TypeResolver) VisitNumberLiteral(p.NumberLiteral) {
	tr.lastType = project.INT
	tr.lastError = nil
}

func (tr *TypeResolver) VisitBoolLiteral(p.BoolLiteral) {
	tr.lastType = project.BOOL
	tr.lastError = nil
}

func (tr *TypeResolver) VisitStringLiteral(p.StringLiteral) {
	tr.lastType = project.STRING
	tr.lastError = nil
}

func (tr *TypeResolver) VisitBrackets(b p.Brackets) {
	newResolver := TypeResolver{gs: tr.gs}
	b.InnerExpression.AcceptVisitor(&newResolver)
	tr.lastType = newResolver.lastType
	tr.lastError = newResolver.lastError
}

func (tr *TypeResolver) VisitAnd(a p.And) {
	tr.validateMatchingPair(a.Lhs, a.Rhs, "and", project.BOOL)
}

func (tr *TypeResolver) VisitOr(o p.Or) {
	tr.validateMatchingPair(o.Lhs, o.Rhs, "or", project.BOOL)
}

func (tr *TypeResolver) VisitEqual(e p.Equal) {
	// incorrect implementation
	tr.validateMatchingPair(e.Lhs, e.Rhs, "equal", project.STRING)
}

func (tr *TypeResolver) VisitLessThan(lt p.LessThan) {
	tr.validateMatchingPair(lt.Lhs, lt.Rhs, "less_than", project.INT)
}

func (tr *TypeResolver) VisitMoreThan(mt p.MoreThan) {
	tr.validateMatchingPair(mt.Lhs, mt.Rhs, "more_than", project.INT)
}

func (tr *TypeResolver) validateMatchingPair(lhs p.Expression,
	rhs p.Expression,
	operand string,
	varType project.GameStateVariableType) {

	lhsResolver := TypeResolver{gs: tr.gs}
	rhsResolver := TypeResolver{gs: tr.gs}
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
