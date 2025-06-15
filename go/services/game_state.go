package services

import (
	"fmt"
	"maps"
	"sort"
	"strings"

	"punchcafe.dev/gb-vngine/project"
)

// A c-compliant variable name
type SourceVariableName string

// A c variable type
type SourceVariableType string

type GameStateService struct {
	gameState project.GameState
}

func NewGameStateService(gameState project.GameState) (*GameStateService, error) {
	// long term this will need to detect collisions
	return &GameStateService{gameState: gameState}, nil
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
	lower := strings.ToLower(string(variableName))
	sanitised := strings.ReplaceAll(lower, "-", "_")
	return SourceVariableName(sanitised), nil
}
