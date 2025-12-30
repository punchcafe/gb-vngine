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

	projectLayer, err := buildProjectLayer(a)

	if err != nil {
		return "", err
	}

	servicesLayer, err := buildServicesLayer(projectLayer)

	if err != nil {
		return "", err
	}

	// Prepare ComponentRenderers

	gameStateRender := render.NewGameStateRenderer(&servicesLayer.gameState)
	componentRenders := []render.ComponentRenderer{gameStateRender, render.MainRenderer, render.TypeDefinitionRenderer}

	renderer, err := render.Build(componentRenders)

	if err != nil {
		return "", err
	}

	return renderer.Render()
}

type projectLayer struct {
	gameState project.GameState
}

type ServicesLayer struct {
	gameState services.GameStateService
}

func buildProjectLayer(app App) (*projectLayer, error) {

	rawGameStateYaml := map[string]string{}
	err := yaml.Unmarshal([]byte(app.GameStateVariables), rawGameStateYaml)
	if err != nil {
		return nil, fmt.Errorf("unable to unmarshall yaml file")
	}

	gameState, err := project.ParseGameState(rawGameStateYaml)
	if err != nil {
		return nil, err
	}

	return &projectLayer{gameState: gameState}, nil
}

func buildServicesLayer(projectLayer *projectLayer) (*ServicesLayer, error) {
	service, err := services.NewGameStateService(projectLayer.gameState)
	if err != nil {
		return nil, err
	}

	return &ServicesLayer{gameState: *service}, nil
}
