package services

import "fmt"

func FIXTURE_StringRegistry() StringRegistry {
	return StringRegistry{
		entries: []StringEntry{
			{"STRING_REG_1", "sample_constant_1"},
			{"STRING_REG_2", "sample_constant_2"},
			{"STRING_REG_3", "sample_constant_3"},
		},
	}
}

func FIXTURE_StringRegistryFrom(stringValues []string) StringRegistry {
	entries := []StringEntry{}
	for i, s := range stringValues {
		entries = append(entries, StringEntry{fmt.Sprintf("STRING_REG_%d", i+1), s})
	}

	return StringRegistry{
		entries: entries,
	}
}
