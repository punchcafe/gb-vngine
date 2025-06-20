package gamestatepredicate

import (
	"fmt"
	"strings"
)

func tokenize(input string) ([]string, error) {

	stringBuilder := strings.Builder{}
	inString := false
	tokens := make([]string, 0)
	for i, char := range input {
		previousRuneIsEscapeCharacter := false
		if i > 0 && (input[i-1] == '\\') {
			previousRuneIsEscapeCharacter = true
		}
		if char == '"' && !previousRuneIsEscapeCharacter {
			// need to check if escaped
			prevInString := inString
			inString = !inString
			if prevInString {
				tokens = append(tokens, stringBuilder.String())
				stringBuilder.Reset()
				continue
			} else {
				continue
			}
		}
		if inString {
			stringBuilder.WriteRune(char)
			continue
		}
		if char == ' ' {
			if stringBuilder.Len() == 0 {
				continue
			} else {
				tokens = append(tokens, stringBuilder.String())
				stringBuilder.Reset()
				continue
			}
		}
		stringBuilder.WriteRune(char)
	}
	if inString {
		return nil, fmt.Errorf("Unterminated string")
	}

	if stringBuilder.Len() > 0 {
		tokens = append(tokens, stringBuilder.String())
	}

	return tokens, nil
}
