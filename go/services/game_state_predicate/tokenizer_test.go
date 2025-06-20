package gamestatepredicate

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestParseGameState(t *testing.T) {
	t.Run("Can correctly parse string maps", func(t *testing.T) {
		result, _ := tokenize("hello, world \"i'm in a string\"")
		assert.Equal(t, []string{"hello,", "world", "i'm in a string"}, result)
	})

	t.Run("Consecutive white space is ignored", func(t *testing.T) {
		result, _ := tokenize(" hello    world  i'm full of    whitespace   ")
		assert.Equal(t, []string{"hello", "world", "i'm", "full", "of", "whitespace"}, result)
	})

	t.Run("escaped string delimiters are included in the string", func(t *testing.T) {
		result, _ := tokenize("hello, world \"i'm a \\\"text quote\\\"\"")
		assert.Equal(t, []string{"hello,", "world", "i'm a \\\"text quote\\\""}, result)
		result, _ = tokenize("\"\\\"hello, world\\\"\" shouldn't break the token")
		assert.Equal(t, []string{"\\\"hello, world\\\"", "shouldn't", "break", "the", "token"}, result)

	})
}
