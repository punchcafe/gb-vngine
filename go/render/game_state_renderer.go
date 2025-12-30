package render

import (
	"fmt"
	"strings"

	"punchcafe.dev/gb-vngine/services"
)

const template = `
struct GameState {
%s
};
`

const GAME_STATE_RENDERER_NAME = "GAME_STATE_RENDERER"

type GameStateRender struct {
	gameStateService *services.GameStateService
}

func NewGameStateRenderer(gss *services.GameStateService) *GameStateRender {
	return &GameStateRender{gameStateService: gss}
}

func (r *GameStateRender) Render() (string, error) {
	var b strings.Builder
	allVariables := r.gameStateService.AllVariables()

	for _, key := range allVariables {
		fieldName, err := r.gameStateService.LookupFieldName(key)
		if err != nil {
			return "", err
		}
		fieldType, err := r.gameStateService.LookupFieldType(key)
		if err != nil {
			return "", err
		}

		b.Write([]byte(fmt.Sprintf("    %s %s;\n", fieldType, fieldName)))
	}
	return fmt.Sprintf(template, b.String()), nil
}

func (r *GameStateRender) Name() string {
	return GAME_STATE_RENDERER_NAME
}

func (r *GameStateRender) Dependencies() []string {
	return []string{}
}
