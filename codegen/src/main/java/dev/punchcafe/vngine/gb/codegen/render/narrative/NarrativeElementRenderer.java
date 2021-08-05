package dev.punchcafe.vngine.gb.codegen.render.narrative;

import dev.punchcafe.vngine.gb.codegen.csan.NarrativeName;
import dev.punchcafe.vngine.gb.codegen.narrative.*;
import lombok.Builder;

import java.util.Optional;

@Builder
public class NarrativeElementRenderer implements NarrativeElementVisitor<String> {

    private final int index;
    private final String narrativeId;

    @Override
    public String visitClearText(ClearText clearText) {
        return String.format("struct NarrativeElement %s = {0x00, CLEAR_TEXT};",
                NarrativeName.elementName(narrativeId, index));
    }

    @Override
    public String visitClearForeground(ClearForeground clearText) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visitSetForeground(SetForeground setForeground) {
        final var body = renderSetForegroundBody(setForeground);
        final String narrativeElement = narrativeElementForType("FOREGROUND");
        return body + "\n" + narrativeElement;
    }

    private String renderSetForegroundBody(SetForeground setForeground){
        switch (setForeground.getAlignment().toLowerCase()){
            case "left":
                return String.format("struct ForegroundElement %s = {LEFT, %s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        setForeground.getSrc());
            case "right":
                return String.format("struct ForegroundElement %s = {RIGHT, %s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        setForeground.getSrc());
            case "center":
                return String.format("struct ForegroundElement %s = {CENTER, %s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        setForeground.getSrc());
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitDelay(Delay delay) {
        final var body = String.format("struct Pause %s = {%d};",
                NarrativeName.elementBodyName(narrativeId, index),
                delay.getSeconds());
        final String narrativeElement = narrativeElementForType("DELAY");
        return body + "\n" + narrativeElement;
    }


    @Override
    public String visitText(Text text) {
        final var body = String.format("struct Text %s = {\"%s\"};",
                NarrativeName.elementBodyName(narrativeId, index),
                Optional.ofNullable(text.getText()).orElse(""));
        final String narrativeElement = narrativeElementForType("TEXT");
        return body + "\n" + narrativeElement;
    }

    private String narrativeElementForType(final String cEnumType){
        return String.format("struct NarrativeElement %s = {&%s, %s};",
                NarrativeName.elementName(narrativeId, index),
                NarrativeName.elementBodyName(narrativeId, index),
                cEnumType);
    }
}
