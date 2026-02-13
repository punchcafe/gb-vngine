package mutation

import (
	"fmt"

	"punchcafe.dev/gb-vngine/services/expression"
)

type Service struct {
	expressionService *expression.Service
}

func (s *Service) StatementIdentifier(ms MutationStatement) (string, error) {
	switch v := ms.(type) {
	case SetFunction:
		expressionIdentifier, expressionErr := s.expressionService.ConvertExpressionToHandleName(v.newValue)
		variableIdentifier, variableErr := s.expressionService.ConvertExpressionToHandleName(v.variable)
		if variableErr != nil || expressionErr != nil {
			return "", fmt.Errorf("unable to parse set mutation statement")
		}
		return fmt.Sprintf("MUTATION_SET_%s_%s", variableIdentifier, expressionIdentifier), nil
	case AddFunction:
		variableIdentifier, variableErr := s.expressionService.ConvertExpressionToHandleName(v.variable)
		if variableErr != nil {
			return "", fmt.Errorf("unable to parse add mutation statement: %s", variableErr.Error())
		}
		return fmt.Sprintf("MUTATION_ADD_%s_%d", variableIdentifier, v.amount), nil

	default:
		panic("unexpected error")
	}
}
