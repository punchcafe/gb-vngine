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
