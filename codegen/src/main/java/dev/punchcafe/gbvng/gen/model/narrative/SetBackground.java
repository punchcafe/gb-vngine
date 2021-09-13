package dev.punchcafe.gbvng.gen.model.narrative;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@NoArgsConstructor
public class SetBackground implements NarrativeElement {

    @Attribute(name = "src")
    private String src;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitSetBackground(this);
    }
}
