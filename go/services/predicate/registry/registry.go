package registry

import (
	"fmt"
	"slices"

	"punchcafe.dev/gb-vngine/project"
	p "punchcafe.dev/gb-vngine/services/predicate"
	"punchcafe.dev/gb-vngine/services/predicate/parse"
)

type Registry struct {
	// Use a map to guarantee distinctness
	// Using a plain list instead of an actual set with insertion ordering to make tests predictable
	// TODO: think about better ways to do this.
	AllPredicates []p.Expression
}

func (r *Registry) Lookup(rawExpression string) (p.Expression, error) {
	expression, err := parseRawExpression(rawExpression)
	if err != nil {
		return nil, err
	}
	if !slices.Contains(r.AllPredicates, expression) {
		return nil, fmt.Errorf("expression not in registry")
	}
	return expression, nil
}

func (r *Registry) AllRegisteredPredicates() []p.Expression {
	return r.AllPredicates
}

func (r *Registry) addRawExpression(rawExpression string) error {
	expression, err := parseRawExpression(rawExpression)
	if err != nil {
		return err
	}

	if slices.Contains(r.AllPredicates, expression) {
		return nil
	}

	r.AllPredicates = append(r.AllPredicates, expression)

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

// TODO: make this a reference
func FromChapter(c project.Chapter) (*Registry, error) {
	r := &Registry{AllPredicates: []p.Expression{}}

	for _, node := range c.Nodes {
		for _, branch := range node.Branches {
			if branch.PredicateExpression == "" {
				continue
			}
			err := r.addRawExpression(branch.PredicateExpression)
			if err != nil {
				return nil, err
			}
		}
	}

	return r, nil
}
