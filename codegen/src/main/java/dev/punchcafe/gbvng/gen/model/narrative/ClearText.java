package dev.punchcafe.gbvng.gen.model.narrative;

public class ClearText implements NarrativeElement {


    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitClearText(this);
    }
}
