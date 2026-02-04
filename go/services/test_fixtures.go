package services

func FIXTURE_StringRegistry() StringRegistry {
	return StringRegistry{
		registry: map[string]uint{
			"sample_constant_1": 1,
			"sample_constant_2": 1,
			"sample_constant_3": 1,
		},
	}
}

func FIXTURE_StringRegistryFrom(stringNames []string) StringRegistry {
	registry := map[string]uint{}
	for _, s := range stringNames {
		registry[s] = 1
	}

	return StringRegistry{
		registry: registry,
	}
}
