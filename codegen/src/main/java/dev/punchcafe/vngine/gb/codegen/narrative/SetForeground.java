package dev.punchcafe.vngine.gb.codegen.narrative;

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
}
