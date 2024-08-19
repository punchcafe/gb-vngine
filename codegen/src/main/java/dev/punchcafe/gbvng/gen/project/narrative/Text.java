package dev.punchcafe.gbvng.gen.project.narrative;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Element
public class Text implements NarrativeElement {
    @org.simpleframework.xml.Text
    private String text;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitText(this);
    }
}
