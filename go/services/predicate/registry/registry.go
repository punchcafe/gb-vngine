package registry

import (
	"fmt"

	"punchcafe.dev/gb-vngine/project"
	p "punchcafe.dev/gb-vngine/services/predicate"
	"punchcafe.dev/gb-vngine/services/predicate/parse"
)

type Registry struct {
	// Use a map to guarantee distinctness
	allPredicates map[p.Expression]bool
}

func (r *Registry) Lookup(rawExpression string) (p.Expression, error) {
	expression, err := parseRawExpression(rawExpression)
	if err != nil {
		return nil, err
	}
	if !r.allPredicates[expression] {
		return nil, fmt.Errorf("expression not in registry")
	}
	return expression, nil
}

func (r *Registry) addRawExpression(rawExpression string) error {
	expression, err := parseRawExpression(rawExpression)
	if err != nil {
		return err
	}
	r.allPredicates[expression] = true
	return nil
}

func parseRawExpression(rawExpression string) (p.Expression, error) {
	tokens, err := parse.ParseTokens(rawExpression)
	if err != nil {
		return nil, err
	}
	expression, err := parse.ParsePredicate(tokens)
	if err != nil {
		return nil, err
	}
	return expression, nil
}

func FromChapter(c project.Chapter) (*Registry, error) {
	r := &Registry{allPredicates: map[p.Expression]bool{}}

	for _, node := range c.Nodes {
		for _, branch := range node.Branches {
			err := r.addRawExpression(branch.PredicateExpression)
			if err != nil {
				return nil, err
			}
		}
	}

	return r, nil
}
