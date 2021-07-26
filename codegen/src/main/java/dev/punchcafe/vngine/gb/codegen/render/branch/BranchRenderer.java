package dev.punchcafe.vngine.gb.codegen.render.branch;

import dev.punchcafe.vngine.gb.codegen.csan.NodeIdSanitiser;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.predicate.PredicateMethodNameConverter;
import dev.punchcafe.vngine.pom.model.Branch;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.NodeType;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.*;
import static dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

@Builder
@Getter
public class BranchRenderer implements ComponentRenderer {

    private ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        return gameConfig.getChapterConfigs().get(0).getNodes().stream()
                .filter(node -> node.getType() == NodeType.AUTOMATIC)
                .map(this::renderNodesBranches)
                .collect(Collectors.joining("\n"));
    }

    private String renderNodesBranches(final Node node) {
        final var stringBuilder = new StringBuilder();
        final var branches = node.getBranches();
        if (branches == null || branches.size() == 0) {
            return renderGameOverBranch(node.getId());
        }
        for (int i = 0; i < branches.size(); i++) {
            stringBuilder.append(renderBranch(branches.get(i), node.getId(), i))
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private String renderGameOverBranch(final String nodeId) {
        return String.format("struct Branch %s_game_over_branch = {&always_true_predicate, GAME_OVER_NODE_ID};",
                NodeIdSanitiser.sanitiseNodeId(nodeId));
    }

    private String renderBranch(final Branch branch, final String nodeId, final int index) {
        final var branchName = NodeIdSanitiser.sanitiseNodeId(nodeId) + "_branch_" + index;
        final var branchPredicate = PredicateMethodNameConverter.convertPredicateExpression(branch.getPredicateExpression());
        final var targetNode = NodeIdSanitiser.sanitiseNodeId(branch.getNodeId());
        return String.format("struct Branch %s = {&%s, &%s};", branchName, branchPredicate, targetNode);
    }

    @Override
    public List<String> dependencies() {
        return List.of(PLAYER_TRANSITION_DEFINITION_RENDERER_NAME,
                PREDICATES_RENDERER_NAME, NODE_RENDERER_NAME,
                ALWAYS_TRUE_PREDICATE_RENDERER_NAME,
                GAME_OVER_NODE_ID_CONSTANT_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return BRANCH_RENDERER_NAME;
    }
}
