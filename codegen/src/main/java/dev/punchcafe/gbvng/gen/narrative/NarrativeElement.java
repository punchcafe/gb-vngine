package dev.punchcafe.gbvng.gen.narrative;

public interface NarrativeElement {

    <T> T acceptVisitor(final NarrativeElementVisitor<T> visitor);
}
