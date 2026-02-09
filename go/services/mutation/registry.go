package mutation

import (
	"fmt"
	"slices"

	"punchcafe.dev/gb-vngine/project"
)

type Registry struct {
	// TODO: replace with an ordered set
	statements []MutationStatement
	gameState  project.GameState
}

func (r *Registry) AllMutationStatements() []MutationStatement {
	// TODO: make immutable
	return r.statements
}

func (r *Registry) registerRawStatement(statement string) error {
	statementModel, err := ParseStatement(statement)
	if err != nil {
		return err
	}
	err = statementModel.Validate(r.gameState)
	if err != nil {
		return fmt.Errorf("invalid mutation statment (%s): %s", statement, err.Error())
	}
	if slices.ContainsFunc(r.statements, func(existingModel MutationStatement) bool {
		return existingModel == statementModel
	}) {
		return nil
	}

	r.statements = append(r.statements, statementModel)
	return nil
}
