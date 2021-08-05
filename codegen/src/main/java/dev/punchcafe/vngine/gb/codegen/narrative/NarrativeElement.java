package dev.punchcafe.vngine.gb.codegen.narrative;

public interface NarrativeElement {

    <T> T acceptVisitor(final NarrativeElementVisitor<T> visitor);
}
