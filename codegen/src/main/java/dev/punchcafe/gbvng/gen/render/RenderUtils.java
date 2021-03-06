package dev.punchcafe.gbvng.gen.render;

public class RenderUtils {

    public static String addIndentationOfLevel(final String string, final int level){
        final var sb = new StringBuilder();
        for(int i = 0; i < level; i++){
            sb.append("    ");
        }
        return sb.append(string).toString();
    }
}
