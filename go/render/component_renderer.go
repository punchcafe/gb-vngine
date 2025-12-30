package render

type ComponentRenderer interface {
	Render() (string, error)
	Name() string
	Dependencies() []string
}
