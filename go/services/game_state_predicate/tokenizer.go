package gamestatepredicate

import (
	"fmt"
	"strconv"
	"strings"
)

type PredicateStringTokenType int
type PredicateStringToken struct {
	tokenType PredicateStringTokenType
	val       string
}

const (
	UNDEFINED PredicateStringTokenType = iota
	VARIABLE
	BOOL_LITERAL
	INT_LITERAL
	STRING_LITERAL
	EXPRESSION_OPEN
	EXPRESSION_CLOSE
	OPERATOR
)

func ParseTokens(expression string) ([]PredicateStringToken, error) {
	tokens, err := tokenize(expression)
	if err != nil {
		return nil, err
	}
	result := make([]PredicateStringToken, 0)
	for _, token := range tokens {
		parsedToken, err := parseToken(token)
		if err != nil {
			return nil, err
		}
		result = append(result, parsedToken)
	}
	return result, nil
}

func parseToken(token string) (PredicateStringToken, error) {
	predicateStringToken := PredicateStringToken{}
	switch token[0] {
	case '$':
		predicateStringToken.tokenType = VARIABLE
		predicateStringToken.val = token[1:]
		return predicateStringToken, nil
	case '"':
		predicateStringToken.tokenType = STRING_LITERAL
		predicateStringToken.val = token[1 : len(token)-1]
		return predicateStringToken, nil

	case '(':
		predicateStringToken.tokenType = EXPRESSION_OPEN
		return predicateStringToken, nil

	case ')':
		predicateStringToken.tokenType = EXPRESSION_CLOSE
		return predicateStringToken, nil

	}

	if token == "true" || token == "false" {
		predicateStringToken.tokenType = BOOL_LITERAL
		predicateStringToken.val = token
		return predicateStringToken, nil

	}

	_, err := strconv.Atoi(token)
	if err == nil {
		predicateStringToken.tokenType = INT_LITERAL
		predicateStringToken.val = token
		return predicateStringToken, nil
	}

	predicateStringToken.tokenType = OPERATOR
	predicateStringToken.val = token
	return predicateStringToken, nil
}

/*
tokenize takes an initial predicate string and splits it into tokens.
This function is responsible for enforcing the token separation syntax rules.

Once this function has been run, the resulting string slice will only contain meaningful
tokens to the predicate language itself. It will remove all whitespacing, and normalize spacing
between variable names and brackets.
*/
func tokenize(input string) ([]string, error) {

	stringBuilder := strings.Builder{}
	inString := false
	tokens := make([]string, 0)
	for i, char := range input {
		previousRuneIsEscapeCharacter := false
		if i > 0 && (input[i-1] == '\\') {
			previousRuneIsEscapeCharacter = true
		}
		if char == '"' && !previousRuneIsEscapeCharacter {
			// need to check if escaped
			prevInString := inString
			inString = !inString
			stringBuilder.WriteRune('"')
			if prevInString {
				tokens = append(tokens, stringBuilder.String())
				stringBuilder.Reset()
				continue
			} else {
				continue
			}
		}
		if inString {
			stringBuilder.WriteRune(char)
			continue
		}
		if char == ' ' {
			if stringBuilder.Len() == 0 {
				continue
			} else {
				tokens = append(tokens, stringBuilder.String())
				stringBuilder.Reset()
				continue
			}
		}
		if !inString && (char == '(' || char == ')') {
			if stringBuilder.Len() != 0 {
				tokens = append(tokens, stringBuilder.String())
				stringBuilder.Reset()
			}
			tokens = append(tokens, string(char))
			continue
		}
		stringBuilder.WriteRune(char)
	}
	if inString {
		return nil, fmt.Errorf("unterminated string")
	}

	if stringBuilder.Len() > 0 {
		tokens = append(tokens, stringBuilder.String())
	}

	return tokens, nil
}
