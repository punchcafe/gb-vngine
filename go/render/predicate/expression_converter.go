package predicate

import (
	"fmt"

	service "punchcafe.dev/gb-vngine/services/predicate"
)

func ConvertExpressionToSource(e service.Expression) (string, error) {
	v := expressionVisitor{}
	e.AcceptVisitor(&v)
	return v.bodyCode, v.lastError
}

type expressionVisitor struct {
	bodyCode  string
	lastError error
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
	renderBinaryOperator("==", e.Lhs, e.Rhs, ev)
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
	lhs := expressionVisitor{}
	rhs := expressionVisitor{}

	lhsExpression.AcceptVisitor(&lhs)
	if lhs.lastError != nil {
		out.lastError = lhs.lastError
		return
	}
	rhsExpression.AcceptVisitor(&rhs)
	if rhs.lastError != nil {
		out.lastError = rhs.lastError
		return
	}

	out.bodyCode = fmt.Sprintf("%s %s %s", lhs.bodyCode, operatorName, rhs.bodyCode)
	out.lastError = nil
}
