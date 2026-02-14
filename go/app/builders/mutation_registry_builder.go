package builders

import (
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services/mutation"
)

func BuildMutationRegistry(chapter *project.Chapter, gameState project.GameState) (*mutation.Registry, error) {
	registry := mutation.EmptyRegistry(gameState)
	for _, node := range chapter.Nodes {
		for _, mutation_statement := range node.GameStateModifiers {
			err := registry.RegisterRawStatement(string(mutation_statement))
			if err != nil {
				return nil, err
			}
		}
	}
	return registry, nil
}
