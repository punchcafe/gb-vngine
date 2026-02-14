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
		valueIdentifier, valueErr := s.expressionService.ConvertExpressionToHandleName(v.amount)
		if variableErr != nil || valueErr != nil {
			return "", fmt.Errorf("unable to parse add mutation statement")
		}
		return fmt.Sprintf("MUTATION_ADD_%s_%s", variableIdentifier, valueIdentifier), nil

	default:
		panic("unexpected error")
	}
}

func (s *Service) StatementSourceCode(ms MutationStatement) (string, error) {
	switch v := ms.(type) {
	case SetFunction:
		expressionSource, expressionErr := s.expressionService.ConvertExpressionToSource(v.newValue)
		variableSource, variableErr := s.expressionService.ConvertExpressionToSource(v.variable)
		if variableErr != nil || expressionErr != nil {
			return "", fmt.Errorf("unable to parse set mutation statement")
		}
		return fmt.Sprintf("%s = %s;", variableSource, expressionSource), nil
	case AddFunction:
		amountSource, amountErr := s.expressionService.ConvertExpressionToSource(v.amount)
		variableSource, variableErr := s.expressionService.ConvertExpressionToSource(v.variable)
		if variableErr != nil || amountErr != nil {
			return "", fmt.Errorf("unable to parse add mutation statement")
		}
		return fmt.Sprintf("%s += %s;", variableSource, amountSource), nil

	default:
		panic("unexpected error")
	}
}
