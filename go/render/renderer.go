package render

import (
	"fmt"
	"maps"
	"strings"
)

type Renderer struct {
	components map[string]ComponentRenderer

	// Working state

	// TODO: could use different states here to catch cyclical dependencies
	renderStatus  map[string]bool
	resultBuilder *strings.Builder
}

func Build(components []ComponentRenderer) (*Renderer, error) {
	componentMap := map[string]ComponentRenderer{}
	for _, component := range components {
		if componentMap[component.Name()] != nil {
			return nil, fmt.Errorf("duplicate name found in component renderers: %s", component.Name())
		}
		componentMap[component.Name()] = component
	}
	return &Renderer{components: componentMap, renderStatus: map[string]bool{}, resultBuilder: &strings.Builder{}}, nil
}

func (r *Renderer) Render() (string, error) {
	for componentRenderer := range maps.Values(r.components) {
		err := r.recursiveRender(componentRenderer)
		if err != nil {
			return "", err
		}
	}
	return r.resultBuilder.String(), nil
}

func (r *Renderer) recursiveRender(componentRenderer ComponentRenderer) error {
	if r.renderStatus[componentRenderer.Name()] {
		return nil
	}

	for _, componentName := range componentRenderer.Dependencies() {
		if r.renderStatus[componentName] {
			continue
		}
		// Assume all valid here, should validate at construction time.
		r.recursiveRender(r.components[componentName])
	}
	result, err := componentRenderer.Render()
	if err != nil {
		// TODO: may want to wrap this
		return err
	}
	_, err = r.resultBuilder.WriteString("\n" + result)
	if err != nil {
		// TODO: may want to wrap this
		return err
	}
	r.renderStatus[componentRenderer.Name()] = true
	return nil
}
