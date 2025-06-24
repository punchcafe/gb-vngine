package gamestatepredicate

import (
	"fmt"
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

type BracketsParseStrategy int

func (bps BracketsParseStrategy) CanHandle(tokens []PredicateStringToken) bool {
	return tokens[0].TokenType == EXPRESSION_OPEN
}

// TODO: should I make this generic?
func (bps BracketsParseStrategy) Parse(tokens []PredicateStringToken) (
	Expression,
	[]PredicateStringToken,
	error) {

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
