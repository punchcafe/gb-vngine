package dev.punchcafe.vngine.gb.codegen.narrative;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.simpleframework.xml.Attribute;

@Data
@AllArgsConstructor
public class ClearForeground implements NarrativeElement {

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        throw new UnsupportedOperationException();
    }
}
