package mutation

import (
	"fmt"
	"regexp"

	"punchcafe.dev/gb-vngine/services/expression"
)

var statementPattern, _ = regexp.Compile("^(.+)\\((.+),(.+)\\)$")

type statementStructure struct {
	functionName string
	variableName string
	expression   string
}

func ParseStatement(statement string) (MutationStatement, error) {
	structure, err := parseStructure(statement)
	if err != nil {
		return nil, err
	}
	variable, err := parseRawExpression(structure.variableName)
	if err != nil {
		return nil, err
	}
	castVariable, ok := variable.(expression.VariableReference)
	if !ok {
		return nil, fmt.Errorf("first argument to statement not a variable")
	}
	expressionModel, err := parseRawExpression(structure.expression)
	if err != nil {
		return nil, err
	}
	switch structure.functionName {
	case "set":
		return SetFunction{variable: castVariable, newValue: expressionModel}, nil
	case "add":
		castValue, ok := expressionModel.(expression.NumberLiteral)
		if !ok {
			return nil, fmt.Errorf("Invalid argument in add() statement: second argument must be a valid number")
		}
		return AddFunction{variable: castVariable, amount: castValue}, nil
	}
	return nil, fmt.Errorf("invalid function: unknown mutation function: %v", structure.functionName)
}

func parseStructure(statement string) (*statementStructure, error) {
	matches := statementPattern.FindSubmatch([]byte(statement))
	if matches == nil {
		return nil, fmt.Errorf("invalid statement syntax: %s", statement)
	}
	s := statementStructure{}
	s.functionName = string(matches[1])
	s.variableName = string(matches[2])
	s.expression = string(matches[3])
	return &s, nil
}

// TODO: deduplicate
func parseRawExpression(rawExpression string) (expression.Expression, error) {
	tokens, err := expression.ParseTokens(rawExpression)
	if err != nil {
		return nil, err
	}
	expression, err := expression.ParsePredicate(tokens)
	if err != nil {
		return nil, err
	}
	return expression, nil
}
