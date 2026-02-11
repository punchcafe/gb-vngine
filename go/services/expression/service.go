package expression

import (
	"fmt"

	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services"
)

type Service struct {
	gs project.GameState
	sr *services.StringRegistry
}

func BuildService(gs project.GameState, sr *services.StringRegistry) *Service {
	return &Service{gs: gs, sr: sr}
}

func (s *Service) ConvertExpressionToSource(e Expression) (string, error) {
	v := newVisitor(s.gs, s.sr)
	e.AcceptVisitor(&v)
	return v.bodyCode, v.lastError
}

func (s *Service) ConvertExpressionToHandleName(e Expression) (string, error) {
	snc := sourceNameConverter{stringRegistry: s.sr}
	e.AcceptVisitor(&snc)
	if snc.lastError != nil {
		return "", snc.lastError
	}
	return fmt.Sprintf("is_%s", snc.sourceName), nil
}

// Private functions and types

type expressionVisitor struct {
	typeResolver   *typeResolver
	stringRegistry *services.StringRegistry
	bodyCode       string
	lastError      error
}

func (ev *expressionVisitor) VisitVariableReference(vr VariableReference) {
	ev.bodyCode = fmt.Sprintf("game_state->%v", string(vr))
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitNumberLiteral(nl NumberLiteral) {
	ev.bodyCode = fmt.Sprintf("%d", nl)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitBoolLiteral(bl BoolLiteral) {
	ev.bodyCode = fmt.Sprintf("%v", bool(bl))
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitStringLiteral(sl StringLiteral) {
	ref, err := ev.stringRegistry.Reference(string(sl))
	if err != nil {
		panic("unexpected error: string literal not registered in String Registry.")
	}
	ev.bodyCode = ref
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitBrackets(b Brackets) {
	v := expressionVisitor{}
	b.InnerExpression.AcceptVisitor(&v)
	if v.lastError != nil {
		ev.lastError = v.lastError
		return
	}
	ev.bodyCode = fmt.Sprintf("(%s)", v.bodyCode)
	ev.lastError = nil
}

func (ev *expressionVisitor) VisitAnd(a And) {
	ev.renderBinaryOperator("&&", a.Lhs, a.Rhs, ev)
}

func (ev *expressionVisitor) VisitOr(o Or) {
	ev.renderBinaryOperator("||", o.Lhs, o.Rhs, ev)
}

func (ev *expressionVisitor) VisitEqual(e Equal) {
	typ, err := ev.typeResolver.resolveType(e.Lhs)
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

func (ev *expressionVisitor) VisitLessThan(lt LessThan) {
	ev.renderBinaryOperator("<", lt.Lhs, lt.Rhs, ev)
}

func (ev *expressionVisitor) VisitMoreThan(mt MoreThan) {
	ev.renderBinaryOperator(">", mt.Lhs, mt.Rhs, ev)
}

func (ev *expressionVisitor) renderBinaryOperator(operatorName string,
	lhsExpression Expression,
	rhsExpression Expression,
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

func (ev *expressionVisitor) renderExpressionCode(e Expression) (string, error) {
	v := expressionVisitor{typeResolver: ev.typeResolver, stringRegistry: ev.stringRegistry}

	e.AcceptVisitor(&v)
	if v.lastError != nil {
		return "", v.lastError
	}
	return v.bodyCode, nil
}

func newVisitor(gs project.GameState, sr *services.StringRegistry) expressionVisitor {
	v := expressionVisitor{}
	tr := NewtypeResolver(gs)
	v.typeResolver = tr
	v.stringRegistry = sr
	return v
}

// Source Name Converter visitors

type sourceNameConverter struct {
	sourceName     string
	lastError      error
	stringRegistry *services.StringRegistry
}

func (nc *sourceNameConverter) VisitVariableReference(vr VariableReference) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("VAR_%v", vr)
}

func (nc *sourceNameConverter) VisitNumberLiteral(nl NumberLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", nl)
}

func (nc *sourceNameConverter) VisitBoolLiteral(bl BoolLiteral) {
	nc.lastError = nil
	nc.sourceName = fmt.Sprintf("%v", bl)
}

func (nc *sourceNameConverter) VisitStringLiteral(sl StringLiteral) {
	sourceName, err := nc.stringRegistry.Reference(string(sl))
	if err != nil {
		panic(fmt.Sprintf("unexpected error: non-registered string found in expression: %s", sl))
	}

	nc.sourceName = sourceName
}

func (nc *sourceNameConverter) VisitBrackets(b Brackets) {
	v := nc.freshConverter()
	b.InnerExpression.AcceptVisitor(v)
	if v.lastError != nil {
		nc.lastError = v.lastError
	}
	nc.sourceName = fmt.Sprintf("BO_%v_BC", v.sourceName)
}

func (nc *sourceNameConverter) VisitAnd(a And) {
	res, err := nc.renderBinaryOperatorName("AND", a.Lhs, a.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitOr(o Or) {
	res, err := nc.renderBinaryOperatorName("OR", o.Lhs, o.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitEqual(e Equal) {
	res, err := nc.renderBinaryOperatorName("EQUALS", e.Lhs, e.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitLessThan(lt LessThan) {
	res, err := nc.renderBinaryOperatorName("LESS_THAN", lt.Lhs, lt.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) VisitMoreThan(mt MoreThan) {
	res, err := nc.renderBinaryOperatorName("MORE_THAN", mt.Lhs, mt.Rhs)
	if err != nil {
		nc.lastError = err
		return
	}
	nc.sourceName = res
}

func (nc *sourceNameConverter) renderBinaryOperatorName(joinToken string, lhs Expression, rhs Expression) (string, error) {
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
