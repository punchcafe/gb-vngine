package dev.punchcafe.gbvng.gen.adapter;

import java.util.Optional;
import java.util.regex.Pattern;

public class NodeIdSanitiser {

    // Node Ids must be comprised of lower case, alpha numeric characters or hyphens
    final static Pattern ILLEGAL_CHARACTER = Pattern.compile("[^a-z0-9-]");

    /**
     * Sanitise a Node Id to be useable int method names.
     *
     * @param nodeId the YAML node ID.
     * @return the sanitised name
     */
    public static String sanitiseNodeId(final String nodeId){
        if(nodeIdHasIllegalCharacters(nodeId)){
            throw new IllegalArgumentException("Invalid characters");
        }
        return Optional.of(nodeId)
                .map(NodeIdSanitiser::replaceHyphensWithUnderscore)
                .map(NodeIdSanitiser::prependNodeIdPrefix)
                .get();
    }

    private static boolean nodeIdHasIllegalCharacters(final String nodeId){
        return ILLEGAL_CHARACTER.matcher(nodeId).matches();
    }

    private static String replaceHyphensWithUnderscore(final String string){
        return string.replaceAll("-", "_");
    }

    private static String prependNodeIdPrefix(final String string){
        return "NODE_ID_" + string;
    }
}
