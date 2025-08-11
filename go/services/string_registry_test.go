package services

import (
	"testing"

	"github.com/stretchr/testify/assert"
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
