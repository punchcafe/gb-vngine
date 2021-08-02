package dev.punchcafe.vngine.gb.codegen.narrative;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

@Root
@Data
public class Narrative {

    @Attribute(name = "id")
    private String narrativeId;

    @ElementListUnion({
            @ElementList(entry = "text", inline = true, type = Text.class),
            @ElementList(entry = "newline", inline = true, type = NewLine.class),
            @ElementList(entry = "clear_text", inline = true, type = ClearText.class),
            @ElementList(entry = "set_fg", inline = true, type = SetForeground.class),
            @ElementList(entry = "clear_fg", inline = true, type = ClearForeground.class)
    })
    private List<NarrativeElement> elements;

}
