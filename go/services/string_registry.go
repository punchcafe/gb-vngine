package services

import (
	"fmt"
	"slices"
)

type StringRegistry struct {
	// TODO: improve this to use a better insertion-ordered set
	// Alteratively, can order by string constant when retrieving all
	entries []StringEntry
}

func (sr *StringRegistry) Reference(value string) (string, error) {
	index := slices.IndexFunc(sr.entries, func(se StringEntry) bool {
		return se.Value == value
	})

	if index == -1 {
		return "", fmt.Errorf("string value not registered in string registry")
	}
	return sr.entries[index].ConstantName, nil
}

type StringEntry struct {
	ConstantName string
	Value        string
}

func (st *StringRegistry) AllStrings() []StringEntry {
	return st.entries
}

func constantName(index uint) string {
	return fmt.Sprintf("STRING_REG_%d", index)
}

func NewRegistry() StringRegistry {
	return StringRegistry{make([]StringEntry, 0)}
}

// Idempotent
func (sr *StringRegistry) AddString(value string) {
	index := slices.IndexFunc(sr.entries, func(se StringEntry) bool {
		return se.Value == value
	})

	if index != -1 {
		// already added
		return
	}
	entryIndex := uint(len(sr.entries) + 1)
	sr.entries = append(sr.entries, StringEntry{constantName(entryIndex), value})
}
