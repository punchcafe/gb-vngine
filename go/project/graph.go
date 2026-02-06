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
	Prompt              string `yaml:"prompt"`
	PredicateExpression string `yaml:"predicate-expression"`
	NodeID              NodeID `yaml:"node-id"`
}

type Node struct {
	ID                 NodeID              `yaml:"id"`
	GameStateModifiers []GameStateModifier `yaml:"changes"`
	Branches           []Branch            `yaml:"branches"`
	NarrativeID        NarrativeID         `yaml:"narrative-id"`
	NodeType           string              `yaml:"type"`
}

type Chapter struct {
	ChapterID        string   `yaml:"chapter-id"`
	ChapterVariables []string `yaml:"chapter-variables"`
	Nodes            []Node   `yaml:"nodes"`
}
