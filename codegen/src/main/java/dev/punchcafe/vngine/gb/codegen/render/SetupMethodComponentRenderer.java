package dev.punchcafe.vngine.gb.codegen.render;

import dev.punchcafe.vngine.gb.codegen.csan.BranchName;
import dev.punchcafe.vngine.gb.codegen.csan.NodeTransitionName;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.NodeType;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.*;
import static dev.punchcafe.vngine.gb.codegen.render.mutate.GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER;
import static dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

@Builder
public class SetupMethodComponentRenderer implements ComponentRenderer {

    private ProjectObjectModel<?> gameConfig;

    @Override
    public String render() {
        return "\nvoid setup(){\n" +
                renderBranchSetUpStatements() +
                "\n}";
    }

    private String renderBranchSetUpStatements(){
        return gameConfig.getChapterConfigs().get(0).getNodes().stream()
                .filter(node -> node.getType() == NodeType.AUTOMATIC)
                .map(this::renderIndividualBranchSetup)
                .collect(Collectors.joining("\n"));
    }

    private String renderIndividualBranchSetup(final Node node){
        final String transitionName = NodeTransitionName.getTransitionNameForNode(node);
        final String branchesArrayName = BranchName.nodesBranchArrayName(node.getId());
        final int numberOfBranches = getNumberOfBranches(node);
        return String.format("    %s.branches = %s;\n", transitionName, branchesArrayName)
                + String.format("    %s.number_of_branches = %d;", transitionName, numberOfBranches);
    }

    private int getNumberOfBranches(final Node node){
        if(node.getBranches() == null || node.getBranches().size() == 0){
            return 1;
        }
        return node.getBranches().size();
    }

    @Override
    public List<String> dependencies() {
        return List.of(PREDICATES_RENDERER_NAME,
                GAME_STATE_MUTATION_RENDERER,
                BRANCH_RENDERER_NAME,
                PROMPTS_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return SETUP_METHOD_RENDERER_NAME;
    }
}
