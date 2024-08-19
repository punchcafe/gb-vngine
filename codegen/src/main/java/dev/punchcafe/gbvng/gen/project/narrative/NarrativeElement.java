package dev.punchcafe.gbvng.gen.project.narrative;

public interface NarrativeElement {

    <T> T acceptVisitor(final NarrativeElementVisitor<T> visitor);
}
