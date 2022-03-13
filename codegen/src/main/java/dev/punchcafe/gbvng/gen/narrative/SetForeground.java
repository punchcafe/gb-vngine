package dev.punchcafe.gbvng.gen.narrative;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@NoArgsConstructor
public class SetForeground implements NarrativeElement {
    @Attribute(name = "align")
    private String alignment;
    @Attribute(name = "src")
    private String src;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitSetForeground(this);
    }
}
