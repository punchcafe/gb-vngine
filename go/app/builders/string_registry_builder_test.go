package builders

import (
	"testing"

	"github.com/stretchr/testify/assert"
	s "punchcafe.dev/gb-vngine/services"
	e "punchcafe.dev/gb-vngine/services/expression"
	"punchcafe.dev/gb-vngine/services/predicate"
)

func TestBuildStringRegistry(t *testing.T) {
	t.Run("builds a registry from a predicate registry", func(t *testing.T) {
		pr := predicate.FIXTURE_PredicateRegistryFrom([]e.Expression{
			e.And{
				Lhs: e.BoolLiteral(true),
				Rhs: e.BoolLiteral(true),
			},
			e.Equal{
				Lhs: e.StringLiteral("hello"),
				Rhs: e.VariableReference("my_string"),
			},
			e.Equal{
				Lhs: e.StringLiteral("another literal"),
				Rhs: e.StringLiteral("a third literal"),
			},
		})

		expectedStringRegistry := []s.StringEntry{
			{
				ConstantName: "STRING_REG_1",
				Value:        "hello",
			},
			{
				ConstantName: "STRING_REG_2",
				Value:        "another literal",
			},
			{
				ConstantName: "STRING_REG_3",
				Value:        "a third literal",
			},
		}
		sr := BuildStringRegistry(&pr)

		assert.Equal(t, expectedStringRegistry, sr.AllStrings())
	})
}
