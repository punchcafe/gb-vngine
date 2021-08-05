package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import lombok.Builder;
import lombok.Singular;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

public class ScriptRenderer {

    private static String HEADERS = "#include <stdio.h>\n" +
            "#include <gb/gb.h>\n";
    private final Map<String,RenderedComponentWriter> componentNameToWrite;

    @Builder
    private ScriptRenderer(@Singular final List<ComponentRenderer> componentRenderers){
        this.componentNameToWrite = componentRenderers.stream()
                .collect(toMap(ComponentRenderer::componentName, RenderedComponentWriter::from));
    }

    public String render(){
        final var stringBuilder = new StringBuilder();
        componentNameToWrite.values()
                .stream()
                .sorted(Comparator.comparing(writer -> writer.getRenderer().componentName()))
                .forEach(writer -> this.writeOut(writer, stringBuilder));
        return HEADERS + stringBuilder.toString();
    }

    private void writeOut(final RenderedComponentWriter writer, final StringBuilder builder){
        for(final var dependency : writer.getRenderer().dependencies()){
            // Ensure all dependencies are already written
            Optional.of(dependency)
                    .map(this.componentNameToWrite::get)
                    .ifPresentOrElse(dependencyWriter -> writeOut(dependencyWriter, builder),
                            () -> {throw new RuntimeException("Missing dependency");});
        }
        writer.write(builder);
    }
}
