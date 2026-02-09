package predicate

import (
	"fmt"

	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
	e "punchcafe.dev/gb-vngine/services/expression"

	// TODO: I think we need a better namespace for this
	"punchcafe.dev/gb-vngine/services/predicate/validate"
)

func ConvertExpressionToSource(
	e e.Expression,
	gs project.GameState,
	sr *services.StringRegistry,
) (string, error) {
	v := newVisitor(gs, sr)
	e.AcceptVisitor(&v)
	return v.bodyCode, v.lastError
}

// Private functions and types

type expressionVisitor struct {
	typeResolver   *validate.TypeResolver
	stringRegistry *services.StringRegistry
	bodyCode       string
	lastError      error
}

func (ev *expressionVisitor) VisitVariableReference(vr e.VariableReference) {
	ev.bodyCode = fmt.Sprintf("game_state->%v", string(vr))
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitNumberLiteral(nl e.NumberLiteral) {
	ev.bodyCode = fmt.Sprintf("%d", nl)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitBoolLiteral(bl e.BoolLiteral) {
	ev.bodyCode = fmt.Sprintf("%v", bool(bl))
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitStringLiteral(sl e.StringLiteral) {
	ref, err := ev.stringRegistry.Reference(string(sl))
	if err != nil {
		panic("unexpected error: string literal not registered in String Registry.")
	}
	ev.bodyCode = ref
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitBrackets(b e.Brackets) {
	v := expressionVisitor{}
	b.InnerExpression.AcceptVisitor(&v)
	if v.lastError != nil {
		ev.lastError = v.lastError
		return
	}
	ev.bodyCode = fmt.Sprintf("(%s)", v.bodyCode)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitAnd(a e.And) {
	ev.renderBinaryOperator("&&", a.Lhs, a.Rhs, ev)
}

func (ev *expressionVisitor) VisitOr(o e.Or) {
	ev.renderBinaryOperator("||", o.Lhs, o.Rhs, ev)
}

func (ev *expressionVisitor) VisitEqual(e e.Equal) {
	typ, err := ev.typeResolver.ResolveType(e.Lhs)
	if err != nil {
		panic("unexpected error: invalid type in equals operator")
	}
	if typ == project.STRING {
		// We don't need to check both sides, as this part of the code
		// assumes that all operator type resolution validation has passed.
		lhs, err := ev.renderExpressionCode(e.Lhs)
		if err != nil {
			ev.lastError = err
			return
		}
		rhs, err := ev.renderExpressionCode(e.Rhs)
		if err != nil {
			ev.lastError = err
			return
		}

		ev.lastError = nil
		ev.bodyCode = fmt.Sprintf("(strcmp(%s, %s) == 0)", lhs, rhs)
	} else {
		ev.renderBinaryOperator("==", e.Lhs, e.Rhs, ev)
	}
}

func (ev *expressionVisitor) VisitLessThan(lt e.LessThan) {
	ev.renderBinaryOperator("<", lt.Lhs, lt.Rhs, ev)
}

func (ev *expressionVisitor) VisitMoreThan(mt e.MoreThan) {
	ev.renderBinaryOperator(">", mt.Lhs, mt.Rhs, ev)
}

func (ev *expressionVisitor) renderBinaryOperator(operatorName string,
	lhsExpression e.Expression,
	rhsExpression e.Expression,
	out *expressionVisitor,
) {
	lhs, err := ev.renderExpressionCode(lhsExpression)
	if err != nil {
		out.lastError = err
		return
	}
	rhs, err := ev.renderExpressionCode(rhsExpression)
	if err != nil {
		out.lastError = err
		return
	}

	out.bodyCode = fmt.Sprintf("%s %s %s", lhs, operatorName, rhs)
	out.lastError = nil
}

func (ev *expressionVisitor) renderExpressionCode(e e.Expression) (string, error) {
	v := expressionVisitor{typeResolver: ev.typeResolver, stringRegistry: ev.stringRegistry}

	e.AcceptVisitor(&v)
	if v.lastError != nil {
		return "", v.lastError
	}
	return v.bodyCode, nil
}

func newVisitor(gs project.GameState, sr *services.StringRegistry) expressionVisitor {
	v := expressionVisitor{}
	tr := validate.NewTypeResolver(gs)
	v.typeResolver = tr
	v.stringRegistry = sr
	return v
}
