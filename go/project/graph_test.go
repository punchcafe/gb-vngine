package project

import (
	"encoding/json"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestGraph(t *testing.T) {
	t.Run("unmarshalls a valid chapter json", func(t *testing.T) {
		sampleJson, expectedChapter := sampleJson()
		var input Chapter
		err := json.Unmarshal([]byte(sampleJson), &input)
		assert.NoError(t, err)
		assert.Equal(t, expectedChapter, input)
	})
}

func sampleJson() (string, Chapter) {
	inputJson := `
{
  "chapter-id": "ch-01",
  "chapter-variables": [],
  "nodes": [
    {
      "id": "0",
      "type": "PLAYER",
      "narrative-id": "nar_01",
      "game-state-modifiers": [],
      "branches": [
        {
          "prompt": "take a sip",
          "node-id": "sip_1"
        },
        {
          "prompt": "explore the kitchen",
          "node-id": "explore_kitchen"
        }
      ]
    },
    {
      "id": "sip_1",
      "type": "AUTOMATIC",
      "narrative-id": "sip_1",
      "game-state-modifiers": [
        "increase $int.sips by 1"
      ],
      "branches": [
        {
          "predicate-expression": "1 equals 1",
          "node-id": "0_after_sip"
        }
      ]
    }
  ]
}`
	expectedStruct := Chapter{
		ChapterID:        "ch-01",
		ChapterVariables: []string{},
		Nodes: []Node{
			{
				ID:                 "0",
				GameStateModifiers: []GameStateModifier{},
				NarrativeID:        "nar_01",
				NodeType:           "PLAYER",
				Branches: []Branch{
					{Prompt: "take a sip", PredicateExpression: "", NodeID: "sip_1"},
					{Prompt: "explore the kitchen", PredicateExpression: "", NodeID: "explore_kitchen"},
				},
			},
			{
				ID:                 "sip_1",
				GameStateModifiers: []GameStateModifier{"increase $int.sips by 1"},
				Branches: []Branch{
					{Prompt: "", PredicateExpression: "1 equals 1", NodeID: "0_after_sip"},
				},
				NarrativeID: "sip_1",
				NodeType:    "AUTOMATIC"},
		},
	}
	return inputJson, expectedStruct
}
