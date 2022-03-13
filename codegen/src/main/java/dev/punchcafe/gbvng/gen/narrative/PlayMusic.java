package dev.punchcafe.gbvng.gen.narrative;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@NoArgsConstructor
public class PlayMusic implements NarrativeElement {

    @Attribute(name = "src")
    private String source;

    @Override
    public <T> T acceptVisitor(NarrativeElementVisitor<T> visitor) {
        return visitor.visitPlayMusic(this);
    }
}
