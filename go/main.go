package main

import (
	"flag"
	"os"

	"punchcafe.dev/gb-vngine/app"
)

func main() {

	var outputFile = flag.String("o", "game.c", "the output file name.")
	var variablesDefinition = flag.String("gs", "game-state-variables.yml", "the game state variable definition yaml")
	flag.Parse()
	data, err := os.ReadFile(*variablesDefinition)
	if err != nil {
		panic("game state variable definition not found!")
	}

	a := app.App{GameStateVariables: string(data), Chapter: ""}
	result, err := a.Render()

	if err != nil {
		panic(err.Error())
	}

	os.WriteFile(*outputFile, []byte(result), 0644) // todo: undestand this
}
