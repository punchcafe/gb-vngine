package dev.punchcafe.gbvng.gen.project.narrative;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

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
            @ElementList(entry = "set_bg", inline = true, type = SetBackground.class, required = false),
            @ElementList(entry = "clear_fg", inline = true, type = ClearForeground.class, required = false),
            @ElementList(entry = "delay", inline = true, type = ClearForeground.class, required = false),
            @ElementList(entry = "play_music", inline = true, type = PlayMusic.class, required = false)
    })
    private List<NarrativeElement> elements;

}
