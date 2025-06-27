package predicate

type Equal struct {
	Lhs any
	Rhs any
}

func (b Equal) ExpressionType() string {
	return "equals"
}

type MoreThan struct {
	Lhs any
	Rhs any
}

func (b MoreThan) ExpressionType() string {
	return "more_than"
}

type LessThan struct {
	Lhs any
	Rhs any
}

func (b LessThan) ExpressionType() string {
	return "less_than"
}

type Or struct {
	Lhs any
	Rhs any
}

func (b Or) ExpressionType() string {
	return "or"
}

type And struct {
	Lhs any
	Rhs any
}

func (b And) ExpressionType() string {
	return "and"
}

type Brackets struct {
	InnerExpression Expression
}

func (b Brackets) ExpressionType() string {
	return "brackets"
}

type BoolLiteral bool

func (bl BoolLiteral) ExpressionType() string {
	return "bool"
}

type StringLiteral string

func (nl StringLiteral) ExpressionType() string {
	return "string"
}

type NumberLiteral int

func (nl NumberLiteral) ExpressionType() string {
	return "number"
}

type VariableReference string

func (bl VariableReference) ExpressionType() string {
	return "variable"
}

type Expression interface {
	ExpressionType() string
	// TODO: implement
}
