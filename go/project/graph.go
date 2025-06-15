package project

type NodeType int

const (
	INVALID NodeType = iota
	PLAYER
	AUTOMATIC
)

type GameStateModifier string
type NodeID string
type NarrativeID string

type Branch struct {
	Prompt              string `json:"prompt"`
	PredicateExpression string `json:"predicate-expression"`
	NodeID              NodeID `json:"node-id"`
}

type Node struct {
	ID                 NodeID              `json:"id"`
	GameStateModifiers []GameStateModifier `json:"game-state-modifiers"`
	Branches           []Branch            `json:"branches"`
	NarrativeID        NarrativeID         `json:"narrative-id"`
	NodeType           string              `json:"type"`
}

type Chapter struct {
	ChapterID        string   `json:"chapter-id"`
	ChapterVariables []string `json:"chapter-variables"`
	Nodes            []Node   `json:"nodes"`
}
