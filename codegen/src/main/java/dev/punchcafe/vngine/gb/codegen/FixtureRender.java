package dev.punchcafe.vngine.gb.codegen;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FixtureRender implements ComponentRenderer {

    private final String fixture;
    private final List<String> dependencies;
    private final String componentName;

    @Override
    public String render() {
        return fixture;
    }

    @Override
    public List<String> dependencies() {
        return dependencies;
    }

    @Override
    public String componentName() {
        return this.componentName;
    }
}
