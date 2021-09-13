package dev.punchcafe.gbvng.gen.csan;

import dev.punchcafe.vngine.pom.model.Node;

public class PromptName {
    public static String getPromptArrayName(final Node node){
        return String.format("%s_prompts", NodeIdSanitiser.sanitiseNodeId(node.getId()));
    }

    public static String getPromptTargetsArrayName(final Node node){
        return String.format("%s_prompt_targets", NodeIdSanitiser.sanitiseNodeId(node.getId()));
    }
}
