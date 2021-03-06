package dev.punchcafe.gbvng.gen.narrative;

public interface NarrativeElementVisitor<T> {
    T visitClearText(final ClearText clearText);
    T visitClearForeground(final ClearForeground clearText);
    T visitSetForeground(final SetForeground setForeground);
    T visitSetBackground(final SetBackground setBackground);
    T visitDelay(final Delay delay);
    T visitText(final Text text);
    T visitPlayMusic(final PlayMusic playMusic);
}
