package app

import (
	"fmt"

	"gopkg.in/yaml.v3"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/render"
	"punchcafe.dev/gb-vngine/services"
)

type App struct {
	GameStateVariables string
	Chapter            string
}

func (a App) Render() (string, error) {
	rawGameStateYaml := map[string]string{}
	err := yaml.Unmarshal([]byte(a.GameStateVariables), rawGameStateYaml)
	if err != nil {
		return "", fmt.Errorf("unable to unmarshall yaml file")
	}

	gameState, err := project.ParseGameState(rawGameStateYaml)
	if err != nil {
		return "", err
	}
	gameStateService, err := services.NewGameStateService(gameState)
	if err != nil {
		return "", err
	}
	gameStateRender := render.NewGameStateRenderer(gameStateService)

	gameStateCode, err := gameStateRender.Render()
	if err != nil {
		return "", err
	}

	mainCode := render.RenderMain()
	typeDefs := render.RenderTypes()
	return fmt.Sprintf("%s\n%s\n%s", gameStateCode, typeDefs, mainCode), nil
}
