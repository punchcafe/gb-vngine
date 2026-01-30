package registry

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"punchcafe.dev/gb-vngine/project"
	"punchcafe.dev/gb-vngine/services/predicate"
)

func TestFromChapter(t *testing.T) {
	t.Run("Correctly populates an empty chapter", func(t *testing.T) {
		res, err := FromChapter(project.Chapter{})
		assert.NoError(t, err)
		assert.Equal(t, Registry{AllPredicates: map[predicate.Expression]bool{}}, *res)
	})

	t.Run("Correctly populates a chapter with a single, simple branch", func(t *testing.T) {
		res, err := FromChapter(project.Chapter{ChapterID: "sample", Nodes: []project.Node{{Branches: []project.Branch{{PredicateExpression: "true and true"}}}}})
		assert.NoError(t, err)
		assert.Equal(t, Registry{AllPredicates: map[predicate.Expression]bool{
			predicate.And{
				Lhs: predicate.BoolLiteral(true),
				Rhs: predicate.BoolLiteral(true),
			}: true,
		}}, *res)
	})

	t.Run("Correctly populates a chapter with a branch without a predicate", func(t *testing.T) {
		// Will be parsed as empty string
		res, err := FromChapter(project.Chapter{ChapterID: "sample", Nodes: []project.Node{{Branches: []project.Branch{{PredicateExpression: ""}}}}})
		assert.NoError(t, err)
		assert.Equal(t, Registry{AllPredicates: map[predicate.Expression]bool{}}, *res)
	})

	t.Run("Correctly populates a chapter with a several branches and nodes", func(t *testing.T) {
		res, err := FromChapter(project.Chapter{ChapterID: "sample", Nodes: []project.Node{
			{
				ID: "1",
				Branches: []project.Branch{
					{PredicateExpression: "true and true", NodeID: "2"},
					{PredicateExpression: "\"hello\" equals $my_string", NodeID: "3"},
				},
			},
			{
				ID: "2",
				Branches: []project.Branch{
					// Already exisists in above branch, so should only appear once
					{PredicateExpression: "\"hello\" equals $my_string", NodeID: "3"},
				},
			},
			{
				ID: "3",
				Branches: []project.Branch{
					{PredicateExpression: "$my_num equals 1", NodeID: "1"},
				},
			},
		}})
		assert.NoError(t, err)
		assert.Equal(t, Registry{AllPredicates: map[predicate.Expression]bool{
			predicate.And{
				Lhs: predicate.BoolLiteral(true),
				Rhs: predicate.BoolLiteral(true),
			}: true,
			predicate.Equal{
				Lhs: predicate.StringLiteral("hello"),
				Rhs: predicate.VariableReference("my_string"),
			}: true,
			predicate.Equal{
				Lhs: predicate.VariableReference("my_num"),
				Rhs: predicate.NumberLiteral(1),
			}: true,
		}}, *res)
	})
}

func TestLookup(t *testing.T) {

	registry, err := FromChapter(project.Chapter{ChapterID: "sample", Nodes: []project.Node{{Branches: []project.Branch{{PredicateExpression: "true and true"}}}}})
	assert.NoError(t, err)

	t.Run("it returns the expression if it has been registered", func(t *testing.T) {
		exp, err := registry.Lookup("true and true")
		assert.NoError(t, err)
		assert.Equal(t, predicate.And(predicate.And{Lhs: predicate.BoolLiteral(true), Rhs: predicate.BoolLiteral(true)}), exp)
	})

	t.Run("it returns an error if the expression has not been registered", func(t *testing.T) {
		_, err := registry.Lookup("true and false")
		assert.Error(t, err, "expression not in registry")
	})

}

func TestAllRegisteredPredicates(t *testing.T) {
	t.Run("it returns all registered predicates", func(t *testing.T) {
		subject := Registry{AllPredicates: map[predicate.Expression]bool{
			predicate.And{
				Lhs: predicate.BoolLiteral(true),
				Rhs: predicate.BoolLiteral(true),
			}: true,
			predicate.Equal{
				Lhs: predicate.StringLiteral("hello"),
				Rhs: predicate.VariableReference("my_string"),
			}: true,
			predicate.Equal{
				Lhs: predicate.VariableReference("my_num"),
				Rhs: predicate.NumberLiteral(1),
			}: true,
		}}
		AllPredicates := subject.AllRegisteredPredicates()
		AllPredicatesList := []predicate.Expression{}
		// TODO: nicer way for doing this
		for predicate := range AllPredicates {
			AllPredicatesList = append(AllPredicatesList, predicate)
		}
		assert.Equal(t,
			[]predicate.Expression{
				predicate.And{
					Lhs: predicate.BoolLiteral(true),
					Rhs: predicate.BoolLiteral(true),
				},
				predicate.Equal{
					Lhs: predicate.StringLiteral("hello"),
					Rhs: predicate.VariableReference("my_string"),
				},
				predicate.Equal{
					Lhs: predicate.VariableReference("my_num"),
					Rhs: predicate.NumberLiteral(1),
				},
			}, AllPredicatesList)
	})

}
