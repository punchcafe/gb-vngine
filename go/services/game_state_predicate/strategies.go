package gamestatepredicate

import (
	"strconv"
)

type BooleanParseStrategy int

func (bps BooleanParseStrategy) CanHandle(tokens []PredicateStringToken) bool {
	return tokens[0].TokenType == BOOL_LITERAL
}

// TODO: should I make this generic?
func (bps BooleanParseStrategy) Parse(tokens []PredicateStringToken) (
	Expression,
	[]PredicateStringToken,
	error) {
	switch tokens[0].Val {
	case "false":
		return BoolLiteral(false), tokens[1:], nil
	case "true":
		return BoolLiteral(true), tokens[1:], nil
	}
	panic("Failed to parse token when expected.")
}

type NumberParseStrategy int

func (nps NumberParseStrategy) CanHandle(tokens []PredicateStringToken) bool {
	return tokens[0].TokenType == INT_LITERAL
}

// TODO: should I make this generic?
func (nps NumberParseStrategy) Parse(tokens []PredicateStringToken) (
	Expression,
	[]PredicateStringToken,
	error) {
	num, err := strconv.Atoi(tokens[0].Val)
	if err != nil {
		panic("Failed to parse integer token when expected.")
	}

	return NumberLiteral(num), tokens[1:], nil
}
