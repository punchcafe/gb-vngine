package gamestatepredicate

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestParsePredicate(t *testing.T) {
	t.Run("Can correctly parse a valid expressions", func(t *testing.T) {
		result, err := ParseTokens("true")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, BoolLiteral(true), new_result)

		result, err = ParseTokens("false")
		assert.NoError(t, err)
		new_result, err = ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, BoolLiteral(false), new_result)

		result, err = ParseTokens("1")
		assert.NoError(t, err)
		new_result, err = ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, NumberLiteral(1), new_result)
	})

	t.Run("Returns error if root predicate contains multiple expressions", func(t *testing.T) {
		tokens, _ := ParseTokens("true false")
		_, err := ParsePredicate(tokens)
		assert.Error(t, err)
		assert.Equal(t, err.Error(), "unexpected tokens remaining after parsing")
	})
}
