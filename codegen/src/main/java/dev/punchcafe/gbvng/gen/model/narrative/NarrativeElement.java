package dev.punchcafe.gbvng.gen.model.narrative;

public interface NarrativeElement {

    <T> T acceptVisitor(final NarrativeElementVisitor<T> visitor);
}
