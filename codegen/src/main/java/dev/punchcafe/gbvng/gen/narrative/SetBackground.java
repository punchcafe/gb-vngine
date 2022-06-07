package dev.punchcafe.gbvng.gen.narrative;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@NoArgsConstructor
public class SetBackground implements NarrativeElement {

    // TODO: change to use source object
    @Attribute(name = "src")
    private String src;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitSetBackground(this);
    }
}
