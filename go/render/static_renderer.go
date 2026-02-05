package render

import _ "embed"

// Type Definitions

//go:embed static/main.c
var mainDef string

//go:embed static/types.c
var typeDef string

//go:embed static/includes.c
var includes string

type StaticRenderer struct {
	content      string
	name         string
	dependencies []string
}

func (sr *StaticRenderer) Render() (string, error) {
	return sr.content, nil
}

func (sr *StaticRenderer) Name() string {
	return sr.name
}

func (sr *StaticRenderer) Dependencies() []string {
	return sr.dependencies
}

// Instances

var MAIN_RENDERER_NAME string = "MAIN_RENDERER"
var MainRenderer ComponentRenderer = &StaticRenderer{
	content: mainDef,
	name:    MAIN_RENDERER_NAME,
	dependencies: []string{
		TYPE_DEFINITION_RENDERER_NAME,
		GAME_STATE_RENDERER_NAME,
		INCLUDES_RENDERER_NAME,
	},
}

var TYPE_DEFINITION_RENDERER_NAME string = "TYPE_DEFINITION_RENDERER"
var TypeDefinitionRenderer ComponentRenderer = &StaticRenderer{
	content:      typeDef,
	name:         TYPE_DEFINITION_RENDERER_NAME,
	dependencies: []string{GAME_STATE_RENDERER_NAME, INCLUDES_RENDERER_NAME},
}

var INCLUDES_RENDERER_NAME string = "INCLUDES_RENDERER"
var IncludesRenderer ComponentRenderer = &StaticRenderer{
	content:      includes,
	name:         INCLUDES_RENDERER_NAME,
	dependencies: []string{},
}
