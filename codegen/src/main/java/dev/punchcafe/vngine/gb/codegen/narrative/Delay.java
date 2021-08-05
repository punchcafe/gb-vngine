package dev.punchcafe.vngine.gb.codegen.narrative;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Delay implements NarrativeElement {

    private int seconds;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        throw new UnsupportedOperationException();
    }
}
