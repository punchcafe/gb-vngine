package gamestatepredicate

import (
	"fmt"
	"slices"
	"strconv"
)

type ValueParserStrategy int

func (bps ValueParserStrategy) CanHandle(tokens []PredicateStringToken) bool {
	tokenType := tokens[0].TokenType
	return tokenType == STRING_LITERAL || tokenType == VARIABLE
}

func (bps ValueParserStrategy) Parse(tokens []PredicateStringToken, previousExpression Expression) (
	Expression,
	[]PredicateStringToken,
	error) {
	val := tokens[0].Val
	switch tokens[0].TokenType {
	case STRING_LITERAL:
		return StringLiteral(val), tokens[1:], nil
	case VARIABLE:
		return VariableReference(val), tokens[1:], nil
	}
	panic("unexpected token type when trying to parse")
}

type BooleanParseStrategy int

func (bps BooleanParseStrategy) CanHandle(tokens []PredicateStringToken) bool {
	return tokens[0].TokenType == BOOL_LITERAL
}

// TODO: should I make this generic?
func (bps BooleanParseStrategy) Parse(tokens []PredicateStringToken, previousExpression Expression) (
	Expression,
	[]PredicateStringToken,
	error) {

	if previousExpression != nil {
		return nil, nil, fmt.Errorf("unexpected token before literal")
	}

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
func (nps NumberParseStrategy) Parse(tokens []PredicateStringToken, previousExpression Expression) (
	Expression,
	[]PredicateStringToken,
	error) {

	if previousExpression != nil {
		return nil, nil, fmt.Errorf("unexpected token before literal")
	}
	num, err := strconv.Atoi(tokens[0].Val)
	if err != nil {
		panic("Failed to parse integer token when expected.")
	}

	return NumberLiteral(num), tokens[1:], nil
}

type BracketsParseStrategy int

func (bps BracketsParseStrategy) CanHandle(tokens []PredicateStringToken) bool {
	return tokens[0].TokenType == EXPRESSION_OPEN
}

// TODO: should I make this generic?
func (bps BracketsParseStrategy) Parse(tokens []PredicateStringToken, previousExpression Expression) (
	Expression,
	[]PredicateStringToken,
	error) {

	if previousExpression != nil {
		return nil, nil, fmt.Errorf("unexpected token before literal")
	}

	bracketScope := 1
	endIndex := 0
	for i, token := range tokens[1:] {
		switch token.TokenType {
		case EXPRESSION_CLOSE:
			bracketScope -= 1
		case EXPRESSION_OPEN:
			bracketScope += 1
		}
		if bracketScope == 0 {
			endIndex = (i + 1) // account for initial offeset
			break
		}
	}
	if bracketScope != 0 {
		return nil, nil, fmt.Errorf("unterminated brackets in expression")
	}

	// TODO: make the reference passing of this better
	res, err := ParsePredicate(tokens[1:endIndex])
	restOfTokens := tokens[endIndex+1:]

	return Brackets{res}, restOfTokens, err

}

type BiOperatorParseStrategy int

var biOperators = []string{"and", "or", "less_than", "more_than", "equals"}

func (bps BiOperatorParseStrategy) CanHandle(tokens []PredicateStringToken) bool {
	return tokens[0].TokenType == OPERATOR && slices.Contains(biOperators, tokens[0].Val)
}

func (bps BiOperatorParseStrategy) Parse(tokens []PredicateStringToken, previousExpression Expression) (Expression,
	[]PredicateStringToken,
	error) {
	nextExpression, remainder, err := ParseExpression(tokens[1:], nil)
	if err != nil {
		return nil, nil, err
	}
	switch tokens[0].Val {
	case "and":
		return And{previousExpression, nextExpression}, remainder, nil
	case "or":
		return Or{previousExpression, nextExpression}, remainder, nil
	case "less_than":
		return LessThan{previousExpression, nextExpression}, remainder, nil
	case "more_than":
		return MoreThan{previousExpression, nextExpression}, remainder, nil
	case "equals":
		return Equal{previousExpression, nextExpression}, remainder, nil
	}
	panic("unexpected error: couldn't resolve operator type")
}
