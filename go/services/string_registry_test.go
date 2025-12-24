package services

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/services/predicate"
	"punchcafe.dev/gb-vngine/services/predicate/registry"
)

func TestStringRegistry(t *testing.T) {
	t.Run("It adds string idempotently", func(t *testing.T) {
		sr := NewRegistry()
		exampleString := "hello, world!"
		sr.AddString(exampleString)
		originalRef, err := sr.Reference(exampleString)
		assert.NoError(t, err)
		sr.AddString(exampleString)
		newRef, err := sr.Reference(exampleString)
		assert.NoError(t, err)
		assert.Equal(t, originalRef, newRef)
		assert.Equal(t, len(sr.registry), 1)
	})

	t.Run("It adds strings correctly", func(t *testing.T) {
		sr := NewRegistry()
		exampleString := "hello, world!"
		otherString := "other string"
		sr.AddString(exampleString)
		exampleRef, err := sr.Reference(exampleString)
		assert.NoError(t, err)
		assert.Equal(t, "STRING_REG_1", exampleRef)
		sr.AddString(otherString)
		otherRef, err := sr.Reference(otherString)
		assert.NoError(t, err)
		assert.Equal(t, "STRING_REG_2", otherRef)
		assert.Equal(t, []StringEntry([]StringEntry{
			{ConstantName: "STRING_REG_1", Value: "hello, world!"},
			{ConstantName: "STRING_REG_2", Value: "other string"},
		}), sr.AllStrings())
	})
}

func TestBuildStringRegistry(t *testing.T) {
	t.Run("builds a registry from a predicate registry", func(t *testing.T) {
		pr := registry.Registry{AllPredicates: map[predicate.Expression]bool{
			predicate.And{
				Lhs: predicate.BoolLiteral(true),
				Rhs: predicate.BoolLiteral(true),
			}: true,
			predicate.Equal{
				Lhs: predicate.StringLiteral("hello"),
				Rhs: predicate.VariableReference("my_string"),
			}: true,
			predicate.Equal{
				Lhs: predicate.StringLiteral("another literal"),
				Rhs: predicate.StringLiteral("a third literal"),
			}: true,
		}}

		expectedStringRegistry := StringRegistry{registry: map[string]uint{
			"hello":           1,
			"another literal": 2,
			"a third literal": 3,
		}}
		sr := BuildStringRegistry(&pr)

		assert.Equal(t, expectedStringRegistry, *sr)
	})
}
