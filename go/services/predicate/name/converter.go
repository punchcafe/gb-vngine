package name

import (
	"fmt"

	"punchcafe.dev/gb-vngine/services"
	p "punchcafe.dev/gb-vngine/services/predicate"
)

func ExpressionToSourceName(e p.Expression, sr *services.StringRegistry) (string, error) {
	snc := sourceNameConverter{stringRegistry: sr}
	e.AcceptVisitor(&snc)
	if snc.lastError != nil {
		return "", snc.lastError
	}
	return fmt.Sprintf("is_%s", snc.sourceName), nil
}

type sourceNameConverter struct {
	sourceName     string
	lastError      error
	stringRegistry *services.StringRegistry
}

func (nc *sourceNameConverter) VisitVariableReference(vr p.VariableReference) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("VAR_%v", vr)
}

func (nc *sourceNameConverter) VisitNumberLiteral(nl p.NumberLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", nl)
}

func (nc *sourceNameConverter) VisitBoolLiteral(bl p.BoolLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", bl)
}

func (nc *sourceNameConverter) VisitStringLiteral(sl p.StringLiteral) {
	sourceName, err := nc.stringRegistry.Reference(string(sl))
	if err != nil {
		panic(fmt.Sprintf("unexpected error: non-registered string found in expression: %s", sl))
	}

	nc.sourceName = sourceName
}

func (nc *sourceNameConverter) VisitBrackets(b p.Brackets) {
	v := nc.freshConverter()
	b.InnerExpression.AcceptVisitor(v)
	if v.lastError != nil {
		nc.lastError = v.lastError
	}
	nc.sourceName = fmt.Sprintf("BO_%v_BC", v.sourceName)
}

func (nc *sourceNameConverter) VisitAnd(a p.And) {
	res, err := nc.renderBinaryOperatorName("AND", a.Lhs, a.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitOr(o p.Or) {
	res, err := nc.renderBinaryOperatorName("OR", o.Lhs, o.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitEqual(e p.Equal) {
	res, err := nc.renderBinaryOperatorName("EQUALS", e.Lhs, e.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitLessThan(lt p.LessThan) {
	res, err := nc.renderBinaryOperatorName("LESS_THAN", lt.Lhs, lt.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitMoreThan(mt p.MoreThan) {
	res, err := nc.renderBinaryOperatorName("MORE_THAN", mt.Lhs, mt.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) renderBinaryOperatorName(joinToken string, lhs p.Expression, rhs p.Expression) (string, error) {
	lhsVisitor := nc.freshConverter()
	rhsVisitor := nc.freshConverter()
	lhs.AcceptVisitor(lhsVisitor)
	rhs.AcceptVisitor(rhsVisitor)
	if lhsVisitor.lastError != nil {
		return "", lhsVisitor.lastError
	}
	if rhsVisitor.lastError != nil {
		return "", rhsVisitor.lastError
	}
	return fmt.Sprintf("%s_%s_%s", lhsVisitor.sourceName, joinToken, rhsVisitor.sourceName), nil
}

func (sn *sourceNameConverter) freshConverter() *sourceNameConverter {
	return &sourceNameConverter{stringRegistry: sn.stringRegistry}
}
