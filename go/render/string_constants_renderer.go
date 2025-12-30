package render

import (
	"fmt"
	"strings"

	"punchcafe.dev/gb-vngine/services"
)

const STRING_CONSTANTS_RENDERER_NAME = "STRING_CONSTANTS_RENDERER"

type StringConstantsRenderer struct {
	stringRegistry *services.StringRegistry
}

func (scr *StringConstantsRenderer) Name() string {
	return STRING_CONSTANTS_RENDERER_NAME
}

func (scr *StringConstantsRenderer) Dependencies() []string {
	return []string{}
}

func (scr *StringConstantsRenderer) Render() (string, error) {
	stringBuilder := strings.Builder{}
	for _, reference := range scr.stringRegistry.AllStrings() {
		stringBuilder.WriteString(fmt.Sprintf("\n#define %s \"%s\"", reference.ConstantName, reference.Value))
	}
	return stringBuilder.String(), nil
}
