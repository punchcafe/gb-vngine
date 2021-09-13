package dev.punchcafe.gbvng.gen.csan;

import dev.punchcafe.vngine.pom.model.Node;

public class NodeTransitionName {

    public static String getTransitionNameForNode(final Node node){
        return String.format("%s_transition", NodeIdSanitiser.sanitiseNodeId(node.getId()));
    }
}
