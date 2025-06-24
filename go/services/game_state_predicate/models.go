package gamestatepredicate

import (
	"fmt"
)

type Identity struct {
	value any
}

type Equal struct {
	lhs any
	rhs any
}

type MoreThan struct {
	lhs any
	rhs any
}

type LessThan struct {
	lhs any
	rhs any
}

type Or struct {
	lhs any
	rhs any
}

type And struct {
	lhs any
	rhs any
}

type Brackets struct {
	operands Expression
}

func (b Brackets) ExpressionType() string {
	return "brackets"
}

type BoolLiteral bool
type StringLiteral string
type NumberLiteral int

func (nl NumberLiteral) ExpressionType() string {
	return "number"
}

type VariableReference string

func (bl BoolLiteral) ExpressionType() string {
	return "bool"
}

type Expression interface {
	ExpressionType() string
	// TODO: implement
}

type ParseStrategy interface {
	CanHandle(tokens []PredicateStringToken) bool
	// TODO: should I make this generic?
	Parse(tokens []PredicateStringToken) (Expression, []PredicateStringToken, error)
}

type PredicateParserStrategies struct {
	strategies []ParseStrategy
}

func (pps *PredicateParserStrategies) GetStrategy(tokens []PredicateStringToken) (ParseStrategy, error) {
	for _, s := range pps.strategies {
		if s.CanHandle(tokens) {
			return s, nil
		}
	}
	return nil, fmt.Errorf("unexpected token")
}

var parserStrategies PredicateParserStrategies = PredicateParserStrategies{[]ParseStrategy{
	// Note that ordering in this configuration is crucial
	BracketsParseStrategy(1),
	BooleanParseStrategy(1),
	NumberParseStrategy(1),
}}

func ParsePredicate(tokens []PredicateStringToken) (Expression, error) {
	// TODO: extract to configurable object
	strategy, err := parserStrategies.GetStrategy(tokens)
	if err != nil {
		return nil, err
	}

	result, remainder, err := strategy.Parse(tokens)
	if len(remainder) != 0 {
		return nil, fmt.Errorf("unexpected tokens remaining after parsing")
	}
	return result, err
}
