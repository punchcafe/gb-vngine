package dev.punchcafe.vngine.gb.codegen.narrative;

public interface NarrativeElementVisitor<T> {
    T visitClearText(final ClearText clearText);
    T visitClearForeground(final ClearForeground clearText);
    T visitSetForeground(final SetForeground setForeground);
    T visitDelay(final Delay delay);
    T visitText(final Text text);
}
