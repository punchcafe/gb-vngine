package dev.punchcafe.gbvng.gen.render;

import dev.punchcafe.gbvng.gen.config.FontConfig;
import dev.punchcafe.gbvng.gen.render.mutate.GameStateMutationRenderer;
import dev.punchcafe.gbvng.gen.csan.BranchName;
import dev.punchcafe.gbvng.gen.csan.NodeTransitionName;
import dev.punchcafe.gbvng.gen.csan.PromptName;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.NodeType;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;
import static dev.punchcafe.gbvng.gen.render.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

@Builder
public class SetupMethodComponentRenderer implements ComponentRenderer {

    private ProjectObjectModel<?> gameConfig;
    private FontConfig fontConfig;

    @Override
    public String render() {
        return "\nvoid setup(){\n" +
                renderBranchSetUpStatements() + "\n" +
                renderPromptSetupStatements() + "\n" +
                String.format("    initialise_font(%s, %d);",
                        fontConfig.getDefaultFont(),
                        fontConfig.getSanitisedCharacterSet().length) +
                "\n}";
    }

    private String renderPromptSetupStatements(){
        return gameConfig.getChapterConfigs().get(0).getNodes().stream()
                .filter(node -> node.getType() == NodeType.PLAYER)
                .map(this::renderIndividualPromptSetup)
                .collect(Collectors.joining("\n"));
    }

    private String renderIndividualPromptSetup(final Node node){
        final var transitionVariableName = NodeTransitionName.getTransitionNameForNode(node);
        final String branchesArrayName = BranchName.nodesBranchArrayName(node.getId());
        final var promptsNames = PromptName.getPromptArrayName(node);
        final var numberOfPrompts = node.getBranches().size();
        return String.format("    %s.branches = %s;\n", transitionVariableName, branchesArrayName)
                + String.format("    %s.prompts = %s;\n", transitionVariableName, promptsNames)
                + String.format("    %s.number_of_branches = %d;", transitionVariableName, numberOfPrompts);
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
                GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER,
                BRANCH_RENDERER_NAME,
                PROMPTS_RENDERER_NAME,
                TEXT_RENDERER_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return SETUP_METHOD_RENDERER_NAME;
    }
}
