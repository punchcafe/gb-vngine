package predicate

import (
	"fmt"

	"punchcafe.dev/gb-vngine/project"
	service "punchcafe.dev/gb-vngine/services/predicate"

	// TODO: I think we need a better namespace for this
	"punchcafe.dev/gb-vngine/services/predicate/validate"
)

func ConvertExpressionToSource(e service.Expression, gs project.GameState) (string, error) {
	v := expressionVisitor{}
	tr := validate.NewTypeResolver(gs)
	v.typeResolver = tr
	e.AcceptVisitor(&v)
	return v.bodyCode, v.lastError
}

type expressionVisitor struct {
	typeResolver *validate.TypeResolver
	bodyCode     string
	lastError    error
}

func (ev *expressionVisitor) VisitVariableReference(vr service.VariableReference) {
	ev.bodyCode = fmt.Sprintf("game_state->%v", string(vr))
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitNumberLiteral(nl service.NumberLiteral) {
	ev.bodyCode = fmt.Sprintf("%d", nl)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitBoolLiteral(bl service.BoolLiteral) {
	ev.bodyCode = fmt.Sprintf("%v", bool(bl))
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitStringLiteral(sl service.StringLiteral) {
	ev.bodyCode = fmt.Sprintf("\"%v\"", sl)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitBrackets(b service.Brackets) {
	v := expressionVisitor{}
	b.InnerExpression.AcceptVisitor(&v)
	if v.lastError != nil {
		ev.lastError = v.lastError
		return
	}
	ev.bodyCode = fmt.Sprintf("(%s)", v.bodyCode)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitAnd(a service.And) {
	renderBinaryOperator("&&", a.Lhs, a.Rhs, ev)
}

func (ev *expressionVisitor) VisitOr(o service.Or) {
	renderBinaryOperator("||", o.Lhs, o.Rhs, ev)
}

func (ev *expressionVisitor) VisitEqual(e service.Equal) {
	typ, err := ev.typeResolver.ResolveType(e.Lhs)
	if err != nil {
		panic("unexpected error: invalid type in equals operator")
	}
	if typ == project.STRING {
		// We don't need to check both sides, as this part of the code
		// assumes that all operator type resolution validation has passed.
		lhs, err := renderExpressionCode(e.Lhs)
		if err != nil {
			ev.lastError = err
			return
		}
		rhs, err := renderExpressionCode(e.Rhs)
		if err != nil {
			ev.lastError = err
			return
		}

		ev.lastError = nil
		ev.bodyCode = fmt.Sprintf("str_compare(%s, %s)", lhs, rhs)
	} else {
		renderBinaryOperator("==", e.Lhs, e.Rhs, ev)
	}
}

func (ev *expressionVisitor) VisitLessThan(lt service.LessThan) {
	renderBinaryOperator("<", lt.Lhs, lt.Rhs, ev)
}

func (ev *expressionVisitor) VisitMoreThan(mt service.MoreThan) {
	renderBinaryOperator(">", mt.Lhs, mt.Rhs, ev)
}

func renderBinaryOperator(operatorName string,
	lhsExpression service.Expression,
	rhsExpression service.Expression,
	out *expressionVisitor,
) {
	lhs, err := renderExpressionCode(lhsExpression)
	if err != nil {
		out.lastError = err
		return
	}
	rhs, err := renderExpressionCode(rhsExpression)
	if err != nil {
		out.lastError = err
		return
	}

	out.bodyCode = fmt.Sprintf("%s %s %s", lhs, operatorName, rhs)
	out.lastError = nil
}

func renderExpressionCode(e service.Expression) (string, error) {
	v := expressionVisitor{}

	e.AcceptVisitor(&v)
	if v.lastError != nil {
		return "", v.lastError
	}
	return v.bodyCode, nil
}
