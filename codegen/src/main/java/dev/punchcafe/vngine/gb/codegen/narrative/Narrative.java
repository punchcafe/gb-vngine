package dev.punchcafe.vngine.gb.codegen.narrative;

import lombok.Data;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

@Root
@Data
public class Narrative {

    @Attribute(name = "id")
    private String narrativeId;

    @ElementListUnion(value = {
            @ElementList(entry = "text", inline = true, type = Text.class, required = false),
            @ElementList(entry = "newline", inline = true, type = NewLine.class, required = false),
            @ElementList(entry = "clear_text", inline = true, type = ClearText.class, required = false),
            @ElementList(entry = "set_fg", inline = true, type = SetForeground.class, required = false),
            @ElementList(entry = "clear_fg", inline = true, type = ClearForeground.class, required = false),
            @ElementList(entry = "delay", inline = true, type = ClearForeground.class, required = false)
    })
    private List<NarrativeElement> elements;

}
