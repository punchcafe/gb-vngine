package services

import "fmt"

type StringRegistry struct {
	// treat 0 uint as nil
	registry map[string]uint
}

func NewRegistry() StringRegistry {
	return StringRegistry{make(map[string]uint)}
}

// Idempotent
func (sr *StringRegistry) AddString(value string) {
	lookup := sr.registry[value]
	if lookup != 0 {
		// already added
		return
	}
	sr.registry[value] = uint(len(sr.registry) + 1)
}

func (sr *StringRegistry) Reference(value string) (string, error) {
	lookup := sr.registry[value]
	if lookup == 0 {
		return "", fmt.Errorf("string value not registered in string registry")
	}
	return constantName(sr.registry[value]), nil
}

type StringEntry struct {
	ConstantName string
	Value        string
}

func (st *StringRegistry) AllStrings() []StringEntry {
	res := make([]StringEntry, 0)
	for k, v := range st.registry {
		res = append(res, StringEntry{constantName(v), k})
	}
	return res
}

func constantName(index uint) string {
	return fmt.Sprintf("STRING_REG_%d", index)
}
