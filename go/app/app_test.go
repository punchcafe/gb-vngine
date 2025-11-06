package app

import (
	_ "embed"
	"testing"

	"github.com/stretchr/testify/assert"
)

//go:embed test/chapter-sample.yaml
var chapterSample string

//go:embed test/game-state-variables.yaml
var gameStateVariables string

//go:embed test/expected_source.c
var expectedSource string

func TestApp(t *testing.T) {
	app := App{Chapter: chapterSample, GameStateVariables: gameStateVariables}
	result, err := app.Render()
	assert.NoError(t, err)
	assert.Equal(t, expectedSource, result)
}
