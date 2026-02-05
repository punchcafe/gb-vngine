package render

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/services"
)

func TestStringConstantsRender(t *testing.T) {
	t.Run("it correctly renders constant values", func(t *testing.T) {
		// Arrange
		sr := services.FIXTURE_StringRegistry()
		scr := BuildStringConstantsRenderer(&sr)

		// Act
		res, err := scr.Render()

		// Assert
		assert.NoError(t, err)
		assert.Equal(
			t,
			"\n#define STRING_REG_1 \"sample_constant_1\"\n#define STRING_REG_2 \"sample_constant_2\"\n#define STRING_REG_3 \"sample_constant_3\"",
			res,
		)

	})

}
