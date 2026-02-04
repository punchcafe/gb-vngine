package main

import (
	"flag"
	"os"

	"punchcafe.dev/gb-vngine/app"
)

func main() {

	var outputFile = flag.String("o", "game.c", "the output file name.")
	var variablesDefinitionFile = flag.String("gs", "game-state-variables.yml", "the game state variable definition yaml")
	var chapterDefinitionFile = flag.String("c", "chapter.yml", "the chapter yml")
	flag.Parse()
	variablesDefinition, err := os.ReadFile(*variablesDefinitionFile)
	if err != nil {
		panic("game state variable definition not found!")
	}

	chapterDefinition, err := os.ReadFile(*chapterDefinitionFile)
	if err != nil {
		panic("failed to read chapter definition!")
	}

	a := app.App{
		GameStateVariables: string(variablesDefinition),
		Chapter:            string(chapterDefinition),
	}

	result, err := a.Render()

	if err != nil {
		panic(err.Error())
	}

	os.WriteFile(*outputFile, []byte(result), 0644) // todo: undestand this
}
