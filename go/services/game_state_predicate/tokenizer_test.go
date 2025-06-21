package gamestatepredicate

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestTokenizer(t *testing.T) {
	t.Run("Can correctly parse string maps", func(t *testing.T) {
		result, _ := tokenize("hello, world \"i'm in a string\"")
		assert.Equal(t, []string{"hello,", "world", "\"i'm in a string\""}, result)
	})

	t.Run("Consecutive white space is ignored", func(t *testing.T) {
		result, _ := tokenize(" hello    world  i'm full of    whitespace   ")
		assert.Equal(t, []string{"hello", "world", "i'm", "full", "of", "whitespace"}, result)
	})

	t.Run("escaped string delimiters are included in the string", func(t *testing.T) {
		result, _ := tokenize("hello, world \"i'm a \\\"text quote\\\"\"")
		assert.Equal(t, []string{"hello,", "world", "\"i'm a \\\"text quote\\\"\""}, result)
		result, _ = tokenize("\"\\\"hello, world\\\"\" shouldn't break the token")
		assert.Equal(t, []string{"\"\\\"hello, world\\\"\"", "shouldn't", "break", "the", "token"}, result)

	})

	t.Run("brackets in a string are ignored", func(t *testing.T) {
		assertTokenizerResult(t, "(open) and \"()\"", []string{"(", "open", ")", "and", "\"()\""})
	})

	t.Run("unterminated string returns an error", func(t *testing.T) {
		_, err := tokenize("hello, world \"i'm unterminated")
		assert.Error(t, err)
		assert.Equal(t, "unterminated string", err.Error())
	})

	t.Run("it splits brackets", func(t *testing.T) {
		assertTokenizerResultTable(t, map[string][]string{
			"hello(, )world (\"i'm a \\\"text quote\\\"\"":     {"hello", "(", ",", ")", "world", "(", "\"i'm a \\\"text quote\\\"\""},
			"(var_1 less_than 4 ) or (bool_var is_equal true)": {"(", "var_1", "less_than", "4", ")", "or", "(", "bool_var", "is_equal", "true", ")"},
		})
	})
}

func assertTokenizerResult(t *testing.T, input string, expected []string) {
	assertTokenizerResultTable(t, map[string][]string{input: expected})
}
func assertTokenizerResultTable(t *testing.T, testTable map[string][]string) {
	for input, expectedOutput := range testTable {
		result, err := tokenize(input)
		assert.NoError(t, err)
		assert.Equal(t, expectedOutput, result)
	}
}
