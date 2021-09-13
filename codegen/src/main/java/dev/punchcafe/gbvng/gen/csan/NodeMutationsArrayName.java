package dev.punchcafe.gbvng.gen.csan;

import dev.punchcafe.vngine.pom.model.Node;

public class NodeMutationsArrayName {
    public static String getMutationArrayForNode(final Node node){
        return String.format("%s_mutation_array", NodeIdSanitiser.sanitiseNodeId(node.getId()));
    }
}
