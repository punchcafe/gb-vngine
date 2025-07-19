package render

import _ "embed"

//go:embed static/main.c
var mainDef string

//go:embed static/types.c
var typeDef string

func RenderMain() string {
	return mainDef
}

func RenderTypes() string {
	return typeDef
}
