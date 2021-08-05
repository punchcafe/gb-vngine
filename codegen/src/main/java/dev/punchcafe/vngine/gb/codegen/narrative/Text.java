package dev.punchcafe.vngine.gb.codegen.narrative;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;

@Data
@NoArgsConstructor
@Element
public class Text implements NarrativeElement {
    private String text;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitText(this);
    }
}
