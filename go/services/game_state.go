package services

import (
	"fmt"
	"maps"
	"regexp"
	"sort"

	"punchcafe.dev/gb-vngine/project"
)

// A c-compliant variable name
type SourceVariableName string

// A c variable type
type SourceVariableType string

type GameStateService struct {
	gameState project.GameState
}

const variableNamePattern string = "^[a-z_][A-Za-z_]*$"

func NewGameStateService(gameState project.GameState) (*GameStateService, error) {
	gss := &GameStateService{gameState: gameState}
	err := gss.validateVariableNames()

	if err != nil {
		return nil, err
	}
	return gss, nil
}

func (gss *GameStateService) validateVariableNames() error {
	// long term this will need to detect collisions
	for variableName := range maps.Keys(gss.gameState) {
		matched, err := regexp.Match(variableNamePattern, []byte(variableName))
		if err != nil {
			panic("Unexpected error during game state parameter validation")
		}
		if !matched {
			return fmt.Errorf("invalid game state variable name: '%s', must follow the pattern: r/%s/", variableName, variableNamePattern)
		}
	}
	return nil
}

func (gss *GameStateService) AllVariables() []project.GameStateVariableName {
	arr := make([]project.GameStateVariableName, 0)
	for k := range maps.Keys(gss.gameState) {
		arr = append(arr, k)
	}
	sort.Slice(arr, func(i, j int) bool {
		return arr[i] < arr[j]
	})
	return arr
}

// Returns what the c-field type should be
func (gss *GameStateService) LookupFieldType(variableName project.GameStateVariableName) (SourceVariableType, error) {
	result, err := gss.gameState.VariableType(variableName)
	if err != nil {
		return "", err
	}
	switch result {
	default:
		return "", fmt.Errorf("invalid type on game state variable: %s", variableName)
	// need to change namespaces here
	case project.INT:
		return "int", nil
	case project.BOOL:
		return "unsigned char", nil
	case project.STRING:
		// this needs to be easier to configure
		return "char *", nil
	}
}

func (gss *GameStateService) LookupFieldName(variableName project.GameStateVariableName) (SourceVariableName, error) {
	_, err := gss.gameState.VariableType(variableName)
	if err != nil {
		return "", fmt.Errorf("unknown game state variable: %s", variableName)
	}
	return SourceVariableName(variableName), nil
}
