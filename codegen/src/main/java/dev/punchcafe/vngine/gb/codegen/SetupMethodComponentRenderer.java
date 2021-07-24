package dev.punchcafe.vngine.gb.codegen;

import java.util.List;

import static dev.punchcafe.vngine.gb.codegen.ComponentRendererName.SETUP_METHOD_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.mutate.GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER;
import static dev.punchcafe.vngine.gb.codegen.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

public class SetupMethodComponentRenderer implements ComponentRenderer {

    @Override
    public String render() {
        return "\nvoid setup(){}";
    }

    @Override
    public List<String> dependencies() {
        return List.of(PREDICATES_RENDERER_NAME, GAME_STATE_MUTATION_RENDERER);
    }

    @Override
    public String componentName() {
        return SETUP_METHOD_RENDERER_NAME;
    }
}
