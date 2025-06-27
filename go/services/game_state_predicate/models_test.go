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

	t.Run("Can correctly parse a valid expression with brackets", func(t *testing.T) {
		result, err := ParseTokens("(1)")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, Brackets{NumberLiteral(1)}, new_result)

		result, err = ParseTokens("(false)")
		assert.NoError(t, err)
		new_result, err = ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, Brackets{BoolLiteral(false)}, new_result)
	})

	t.Run("Can correctly parse an and expression", func(t *testing.T) {
		result, err := ParseTokens("true and false")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, And{BoolLiteral(true), BoolLiteral(false)}, new_result)

		result, err = ParseTokens("true and (true and false)")
		assert.NoError(t, err)
		new_result, err = ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, And{BoolLiteral(true), Brackets{And{BoolLiteral(true), BoolLiteral(false)}}}, new_result)
	})

	t.Run("Can correctly parse an or expression", func(t *testing.T) {
		result, err := ParseTokens("(1 less_than 2) or false")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, Or{Brackets{LessThan{NumberLiteral(1), NumberLiteral(2)}}, BoolLiteral(false)}, new_result)
	})

	t.Run("Can correctly parse an equals expression", func(t *testing.T) {
		result, err := ParseTokens("(1 less_than 2) equals 10")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, Equal{Brackets{LessThan{NumberLiteral(1), NumberLiteral(2)}}, NumberLiteral(10)}, new_result)
	})

	t.Run("Can correctly parse a less_than expression", func(t *testing.T) {
		result, err := ParseTokens("1 less_than 2")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, LessThan{NumberLiteral(1), NumberLiteral(2)}, new_result)
	})

	t.Run("Can correctly parse a more_than expression", func(t *testing.T) {
		result, err := ParseTokens("1 more_than 2")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, MoreThan{NumberLiteral(1), NumberLiteral(2)}, new_result)
	})

	t.Run("Can correctly parse a reference expression", func(t *testing.T) {
		result, err := ParseTokens("1 more_than $int_var")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, MoreThan{NumberLiteral(1), VariableReference("int_var")}, new_result)
	})

	t.Run("Can correctly parse a string expression", func(t *testing.T) {
		result, err := ParseTokens("\"hello\" equals $my_string")
		assert.NoError(t, err)
		new_result, err := ParsePredicate(result)
		assert.NoError(t, err)
		assert.Equal(t, Equal{StringLiteral("hello"), VariableReference("my_string")}, new_result)
	})

	t.Run("Returns error if root predicate contains multiple expressions", func(t *testing.T) {
		tokens, _ := ParseTokens("true false")
		_, err := ParsePredicate(tokens)
		assert.Error(t, err)
		assert.Equal(t, err.Error(), "unexpected token before literal")
	})
}
