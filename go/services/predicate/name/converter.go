package name

import (
	"fmt"

	p "punchcafe.dev/gb-vngine/services/predicate"
)

type SourceNameConverter struct {
	sourceName string
	lastError  error
}

func (nc *SourceNameConverter) VisitVariableReference(vr p.VariableReference) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("VAR_%v", vr)
}

func (nc *SourceNameConverter) VisitNumberLiteral(nl p.NumberLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", nl)
}

func (nc *SourceNameConverter) VisitBoolLiteral(bl p.BoolLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", bl)
}

func (nc *SourceNameConverter) VisitStringLiteral(sl p.StringLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", sl)
}

func (nc *SourceNameConverter) VisitBrackets(b p.Brackets) {
	v := SourceNameConverter{}
	b.InnerExpression.AcceptVisitor(&v)
	if v.lastError != nil {
		nc.lastError = v.lastError
	}
	nc.sourceName = fmt.Sprintf("BO_%v_BC", v.sourceName)
}

func (nc *SourceNameConverter) VisitAnd(a p.And) {
	res, err := renderBinaryOperatorName("AND", a.Lhs, a.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *SourceNameConverter) VisitOr(o p.Or) {
	res, err := renderBinaryOperatorName("OR", o.Lhs, o.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *SourceNameConverter) VisitEqual(e p.Equal) {
	res, err := renderBinaryOperatorName("EQUALS", e.Lhs, e.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *SourceNameConverter) VisitLessThan(lt p.LessThan) {
	res, err := renderBinaryOperatorName("LESS_THAN", lt.Lhs, lt.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *SourceNameConverter) VisitMoreThan(mt p.MoreThan) {
	res, err := renderBinaryOperatorName("MORE_THAN", mt.Lhs, mt.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func ExpressionToSourceName(e p.Expression) (string, error) {
	snc := SourceNameConverter{}
	e.AcceptVisitor(&snc)
	if snc.lastError != nil {
		return "", snc.lastError
	}
	return fmt.Sprintf("is_%s", snc.sourceName), nil
}

func renderBinaryOperatorName(joinToken string, lhs p.Expression, rhs p.Expression) (string, error) {
	lhsVisitor := SourceNameConverter{}
	rhsVisitor := SourceNameConverter{}
	lhs.AcceptVisitor(&lhsVisitor)
	rhs.AcceptVisitor(&rhsVisitor)
	if lhsVisitor.lastError != nil {
		return "", lhsVisitor.lastError
	}
	if rhsVisitor.lastError != nil {
		return "", rhsVisitor.lastError
	}
	return fmt.Sprintf("%s_%s_%s", lhsVisitor.sourceName, joinToken, rhsVisitor.sourceName), nil
}
