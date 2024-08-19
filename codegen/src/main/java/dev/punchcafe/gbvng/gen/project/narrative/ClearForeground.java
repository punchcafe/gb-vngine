package dev.punchcafe.gbvng.gen.project.narrative;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClearForeground implements NarrativeElement {

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        throw new UnsupportedOperationException();
    }
}
