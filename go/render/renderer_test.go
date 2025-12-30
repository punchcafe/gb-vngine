package render

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

// Mocking

type MockComponent struct {
	name         string
	dependencies []string
	content      string
}

func (mc *MockComponent) Render() (string, error) {
	return mc.content, nil
}

func (mc *MockComponent) Name() string {
	return mc.name
}

func (mc *MockComponent) Dependencies() []string {
	return mc.dependencies
}

// Tests

func TestRendererBuild(t *testing.T) {
	t.Run("doesn't allow duplicate renderer component names", func(t *testing.T) {
		a_renderer := MockComponent{name: "a_renderer", dependencies: []string{"b_renderer"}}
		b_renderer := MockComponent{name: "b_renderer", dependencies: []string{}}
		another_b_renderer := MockComponent{name: "b_renderer", dependencies: []string{}}

		_, err := Build([]ComponentRenderer{&a_renderer, &b_renderer, &another_b_renderer})
		assert.Error(t, err)
		assert.Equal(t, "duplicate name found in component renderers: b_renderer", err.Error())

	})

	t.Run("it builds a Renderer", func(t *testing.T) {
		a_renderer := MockComponent{name: "a_renderer", dependencies: []string{"b_renderer", "c_renderer"}}
		b_renderer := MockComponent{name: "b_renderer", dependencies: []string{"c_renderer"}}
		c_renderer := MockComponent{name: "c_renderer", dependencies: []string{}}

		result, err := Build([]ComponentRenderer{&a_renderer, &b_renderer, &c_renderer})
		assert.NoError(t, err)
		assert.NotNil(t, result)
	})
}
func TestRendererRender(t *testing.T) {

	t.Run("it resolves depencies and renders in order", func(t *testing.T) {
		// Arrange
		b_renderer := MockComponent{name: "b_renderer", dependencies: []string{"c_renderer"}, content: "b"}
		c_renderer := MockComponent{name: "c_renderer", dependencies: []string{}, content: "c"}
		a_renderer := MockComponent{name: "a_renderer", dependencies: []string{"b_renderer", "c_renderer"}, content: "a"}

		subject, _ := Build([]ComponentRenderer{&a_renderer, &b_renderer, &c_renderer})

		// Act
		result, err := subject.Render()

		// Assert
		assert.NoError(t, err)
		assert.Equal(t, "\nc\nb\na", result)
	})

}
