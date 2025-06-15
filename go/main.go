package main

import (
	"flag"
	"fmt"
	"os"

	"gopkg.in/yaml.v3"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/render"
	"punchcafe.dev/gb-vngine/services"
)

func main() {
	var outputFile = flag.String("o", "game.c", "the output file name.")
	var variablesDefinition = flag.String("gs", "game-state-variables.yml", "the game state variable definition yaml")
	flag.Parse()
	data, err := os.ReadFile(*variablesDefinition)
	if err != nil {
		panic("game state variable definition not found!")
	}
	rawGameStateYaml := map[string]string{}
	err = yaml.Unmarshal(data, rawGameStateYaml)
	if err != nil {
		panic("unable to unmarshall yaml file")
	}

	gameState, err := project.ParseGameState(rawGameStateYaml)
	if err != nil {
		panic(err.Error())
	}
	gameStateService, err := services.NewGameStateService(gameState)
	if err != nil {
		panic(err.Error())
	}
	gameStateRender := render.NewGameStateRenderer(gameStateService)

	gameStateCode, err := gameStateRender.Render()
	if err != nil {
		panic(err.Error())
	}

	main_code := render.RenderMain()
	os.WriteFile(*outputFile, []byte(fmt.Sprintf("%s\n%s", gameStateCode, main_code)), 0644) // todo: undestand this
}
