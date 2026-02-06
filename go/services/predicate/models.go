package predicate

type Equal struct {
	Lhs Expression
	Rhs Expression
}

func (e Equal) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitEqual(e)
}

type MoreThan struct {
	Lhs Expression
	Rhs Expression
}

func (mt MoreThan) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitMoreThan(mt)
}

type LessThan struct {
	Lhs Expression
	Rhs Expression
}

func (lt LessThan) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitLessThan(lt)
}

type Or struct {
	Lhs Expression
	Rhs Expression
}

func (o Or) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitOr(o)
}

type And struct {
	Lhs Expression
	Rhs Expression
}

func (a And) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitAnd(a)
}

type Brackets struct {
	InnerExpression Expression
}

func (b Brackets) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitBrackets(b)
}

type BoolLiteral bool

func (bl BoolLiteral) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitBoolLiteral(bl)
}

type StringLiteral string

func (sl StringLiteral) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitStringLiteral(sl)
}

type NumberLiteral int

func (nl NumberLiteral) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitNumberLiteral(nl)
}

type VariableReference string

func (vr VariableReference) AcceptVisitor(ev ExpressionVisitor) {
	ev.VisitVariableReference(vr)
}

type ExpressionVisitor interface {
	VisitVariableReference(VariableReference)
	VisitNumberLiteral(NumberLiteral)
	VisitBoolLiteral(BoolLiteral)
	VisitStringLiteral(StringLiteral)
	VisitBrackets(Brackets)
	VisitAnd(And)
	VisitOr(Or)
	VisitLessThan(LessThan)
	VisitMoreThan(MoreThan)
	VisitEqual(Equal)
}

// TODO: extract this to a syntax module
type Expression interface {
	AcceptVisitor(ExpressionVisitor)
}
