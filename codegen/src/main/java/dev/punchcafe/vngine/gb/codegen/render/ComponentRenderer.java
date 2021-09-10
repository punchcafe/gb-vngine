package dev.punchcafe.vngine.gb.codegen.render;

import java.util.List;

public interface ComponentRenderer {

    String render();

    List<String> dependencies();

    String componentName();

    default String renderWithHeader(){
        return this.headerRenderer() + "\n" + this.render();
    }

    private String headerRenderer() {
        return "/*\n" +
                "=================================================================\n" +
                String.format("RENDERED BY: %s\n", this.componentName()) +
                String.format("TYPE: %s\n", this.getClass().getSimpleName()) +
                "=================================================================\n" +
                String.format("DEPENDS ON: \n%s", this.renderDependencies()) +
                "*/";
    }

    private String renderDependencies() {
        final var stringBuilder = new StringBuilder();
        final int namesPerRow = 3;
        for (int i = 0; i < this.dependencies().size() / namesPerRow; i++) {
            for (int x = 0; x < namesPerRow; x++) {
                int index = i * namesPerRow + x;
                if (index >= this.dependencies().size()) {
                    break;
                }
                stringBuilder.append(this.dependencies().get(index));
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
