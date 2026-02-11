package app

import (
	"fmt"

	"gopkg.in/yaml.v3"
	"punchcafe.dev/gb-vngine/app/builders"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/render"
	predicaterender "punchcafe.dev/gb-vngine/render/predicate"
	"punchcafe.dev/gb-vngine/services"
	"punchcafe.dev/gb-vngine/services/expression"
	"punchcafe.dev/gb-vngine/services/predicate"
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

	gameStateRender := render.NewGameStateRenderer(servicesLayer.gameState)
	stringConstantRenderers := render.BuildStringConstantsRenderer(servicesLayer.stringRegistry)
	predicateFunctionsRenderer := predicaterender.BuildFunctionRenderer(
		servicesLayer.predicateRegsitry,
		servicesLayer.expressionService,
	)

	componentRenders := []render.ComponentRenderer{
		gameStateRender,
		predicateFunctionsRenderer,
		stringConstantRenderers,
		render.MainRenderer,
		render.TypeDefinitionRenderer,
		render.IncludesRenderer,
	}

	renderer, err := render.Build(componentRenders)

	if err != nil {
		return "", err
	}

	return renderer.Render()
}

type projectLayer struct {
	gameState project.GameState
	chapter   project.Chapter
}

type ServicesLayer struct {
	gameState         *services.GameStateService
	predicateRegsitry *predicate.Registry
	stringRegistry    *services.StringRegistry
	expressionService *expression.Service
}

func buildProjectLayer(app App) (*projectLayer, error) {

	rawGameStateYaml := map[string]string{}
	err := yaml.Unmarshal([]byte(app.GameStateVariables), rawGameStateYaml)
	if err != nil {
		return nil, fmt.Errorf("unable to unmarshall yaml file")
	}

	chapter := project.Chapter{}
	err = yaml.Unmarshal([]byte(app.Chapter), &chapter)
	if err != nil {
		return nil, fmt.Errorf("unable to unmarshall chapter file")
	}

	gameState, err := project.ParseGameState(rawGameStateYaml)
	if err != nil {
		return nil, err
	}

	return &projectLayer{gameState: gameState, chapter: chapter}, nil
}

func buildServicesLayer(projectLayer *projectLayer) (*ServicesLayer, error) {
	gameStateService, err := services.NewGameStateService(projectLayer.gameState)
	if err != nil {
		return nil, err
	}

	predicateRegistry, err := predicate.FromChapter(projectLayer.chapter)

	if err != nil {
		return nil, err
	}

	stringRegistry := builders.BuildStringRegistry(predicateRegistry)
	expressionService := expression.BuildService(projectLayer.gameState, stringRegistry)

	return &ServicesLayer{
		gameState:         gameStateService,
		predicateRegsitry: predicateRegistry,
		stringRegistry:    stringRegistry,
		expressionService: expressionService,
	}, nil
}
