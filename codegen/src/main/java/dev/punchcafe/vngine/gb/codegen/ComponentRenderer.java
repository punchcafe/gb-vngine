package dev.punchcafe.vngine.gb.codegen;

import java.util.List;

public interface ComponentRenderer {

    String render();
    List<Class<? extends ComponentRenderer>> dependencies();
}
