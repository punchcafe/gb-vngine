package project

import (
	"fmt"
	"strings"
)

type GameStateVariableName string
type GameStateVariableType int

const (
	INT GameStateVariableType = iota
	STRING
	BOOL
)

type GameState map[GameStateVariableName]GameStateVariableType

func ParseGameState(rawMap map[string]string) (*GameState, error) {
	result := map[GameStateVariableName]GameStateVariableType{}
	for k, v := range rawMap {
		gsvt, err := fromName(v)
		if err != nil {
			return nil, err
		}
		result[GameStateVariableName(k)] = gsvt
	}
	castResult := GameState(result)
	return &castResult, nil
}

func fromName(yamlName string) (GameStateVariableType, error) {
	switch strings.ToLower(yamlName) {
	default:
		return 0, fmt.Errorf("invalid game state variable type: %s", yamlName)
	case "int":
		return INT, nil
	case "string":
		return STRING, nil
	case "bool":
		return BOOL, nil
	}
}
