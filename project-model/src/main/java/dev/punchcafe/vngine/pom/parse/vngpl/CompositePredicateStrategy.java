package dev.punchcafe.vngine.pom.parse.vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.composite.AndOrOperation;
import dev.punchcafe.vngine.pom.model.vngpl.composite.CompositeExpression;
import dev.punchcafe.vngine.pom.model.vngpl.composite.PredicateLink;

import java.util.ArrayList;
import java.util.List;

public class CompositePredicateStrategy implements ParsingStrategy {

    private static String AND_JOINER = " AND ";
    private static String OR_JOINER = " OR ";

    @Override
    public PredicateExpression parse(String message, PredicateParser predicateParser) {
        final String trimmedMessage = message.trim();
        if(entireExpressionIsEnclosedInBrackets(trimmedMessage)){
            return parse(trimmedMessage.substring(1,trimmedMessage.length()-1), predicateParser);
        }
        final List<PredicateLink> links = new ArrayList<>();
        int bracketScope = 0;
        Integer currentLinkStartIndex = null;
        AndOrOperation currentJoinOperation = null;
        int i = 0;
        // This loop looks for AND and OR joiners, when not within a bracket, and uses them to extract each expression.
        while (i < trimmedMessage.length()) {
            if (trimmedMessage.substring(i).startsWith(AND_JOINER) && bracketScope == 0) {
                if (currentLinkStartIndex != null) {
                    links.add(PredicateLink.newLink(predicateParser.parse(message.substring(currentLinkStartIndex, i)),
                            currentJoinOperation));
                } else {
                    links.add(PredicateLink.firstLink(predicateParser.parse(message.substring(0, i))));
                }
                currentJoinOperation = AndOrOperation.AND;
                final int endOfAndJoinerIndex = i + AND_JOINER.length();
                currentLinkStartIndex = endOfAndJoinerIndex;
                i = endOfAndJoinerIndex;
                continue;
            } else if (message.substring(i).startsWith(OR_JOINER) && bracketScope == 0) {
                if (currentLinkStartIndex != null) {
                    links.add(PredicateLink.newLink(predicateParser.parse(message.substring(currentLinkStartIndex, i)),
                            currentJoinOperation));
                } else {
                    links.add(PredicateLink.firstLink(predicateParser.parse(message.substring(0, i))));
                }
                currentJoinOperation = AndOrOperation.OR;
                final int endOfOrJoinerIndex = i + OR_JOINER.length();
                currentLinkStartIndex = endOfOrJoinerIndex;
                i = endOfOrJoinerIndex;
                continue;
            }
            bracketScope = getResultantScopeFromCharacter(bracketScope, trimmedMessage.charAt(i));
            i++;
        }
        if(bracketScope != 0){
            throw new InvalidVngplExpression();
        }
        if(!links.isEmpty()){
            // add last element
            links.add(PredicateLink.newLink(predicateParser.parse(message.substring(currentLinkStartIndex)), currentJoinOperation));
            return CompositeExpression.fromLinks(links);
        }
        throw new InvalidVngplExpression();
    }

    private boolean entireExpressionIsEnclosedInBrackets(final String trimmedString){
        if(!trimmedString.startsWith("(") || !trimmedString.endsWith(")")){
            return false;
        }
        // Skip first element, as we know we can increment by one because expression starts with (
        int scope = 1;
        for(int i = 1; i < trimmedString.length() - 1; i++){
            scope = getResultantScopeFromCharacter(scope, trimmedString.charAt(i));
            if(scope < 1){
                // scope has dropped, first bracket has been closed, and we aren't at the last element yet
                return false;
            }
        }
        return scope == 1;
    }

    private int getResultantScopeFromCharacter(final int currentScope, final char character){
        switch (character) {
            case '(':
                return currentScope + 1;
            case ')':
                return currentScope - 1;
            default:
                return currentScope;
        }
    }

    @Override
    public boolean isApplicable(String message) {
        return message.trim().startsWith("(") || message.contains(AND_JOINER) || message.contains(OR_JOINER);
    }

    @Override
    public Integer priority() {
        return 0;
    }
}
