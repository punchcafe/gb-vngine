package dev.punchcafe.vngine.gb.codegen.render.transition;

import dev.punchcafe.vngine.gb.codegen.csan.BranchName;
import dev.punchcafe.vngine.gb.codegen.csan.NodeIdSanitiser;
import dev.punchcafe.vngine.gb.codegen.csan.PromptName;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.model.Branch;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.NodeType;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.NODE_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.PROMPTS_RENDERER_NAME;
import static java.util.stream.Collectors.joining;

@Builder
public class PromptsRenderer implements ComponentRenderer {

    private ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        return gameConfig
                .getChapterConfigs().get(0).getNodes().stream()
                .filter(node -> node.getType() == NodeType.PLAYER)
                .map(this::renderPromptFields)
                .collect(joining("\n"));
    }

    private String renderPromptFields(final Node node){
        final var promptsArray = node.getBranches().stream()
                .map(Branch::getPrompt)
                .map(prompt -> String.format("\"%s\"", prompt))
                .collect(joining(",", "{", "}"));
        final var nodeTargetArray = node.getBranches().stream()
                .map(Branch::getNodeId)
                .map(NodeIdSanitiser::sanitiseNodeId)
                .map(nodeId -> String.format("&%s", nodeId))
                .collect(joining(",", "{", "}"));
        return String.format("char * %s[] = %s;\n", PromptName.getPromptArrayName(node), promptsArray)
                + String.format("struct Node * %s[] = %s;", PromptName.getPromptTargetsArrayName(node), nodeTargetArray);
    }

    @Override
    public List<String> dependencies() {
        return List.of(NODE_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return PROMPTS_RENDERER_NAME;
    }
}
