package dev.punchcafe.gbvng.gen.render.narrative;

import dev.punchcafe.gbvng.gen.model.narrative.*;
import dev.punchcafe.gbvng.gen.csan.NarrativeName;
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

    @Override
    public String visitSetBackground(SetBackground setBackground) {
        final var body = renderSetBackgroundBody(setBackground);
        final String narrativeElement = narrativeElementForType("BACKGROUND");
        return body + "\n" + narrativeElement;
    }

    public static String TILE_ASSIGNMENT_SUFFIX = "_tile_assign";
    public static String TILE_DATA_SUFFIX = "_tile_data";
    public static String TILE_DATA_SIZE_DEFINITION_SUFFIX = "_TILE_DATA_SIZE";

    private String renderSetBackgroundBody(SetBackground setBackground){
        final var elementBodyName = NarrativeName.elementBodyName(narrativeId, index);
        final var backgroundDataName = setBackground.getSrc().concat(TILE_DATA_SUFFIX);
        final var backgroundDataSizeName = setBackground.getSrc().toUpperCase().concat(TILE_DATA_SIZE_DEFINITION_SUFFIX);
        final var backgroundTileAssignmentName = setBackground.getSrc().concat(TILE_ASSIGNMENT_SUFFIX);
        return String.format("struct BackgroundElement %s = {%s, %s, %s};",
                elementBodyName,
                backgroundDataName,
                backgroundDataSizeName,
                backgroundTileAssignmentName);
    }

    private String renderSetForegroundBody(SetForeground setForeground){
        switch (setForeground.getAlignment().toLowerCase()){
            case "left":
                return String.format("struct ForegroundElement %s = {LEFT_PORTRAIT, %s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        setForeground.getSrc());
            case "right":
                return String.format("struct ForegroundElement %s = {RIGHT_PORTRAIT, %s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        setForeground.getSrc());
            case "center":
                return String.format("struct ForegroundElement %s = {CENTER_PORTRAIT, %s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        setForeground.getSrc());
            case "center_focus":
                return String.format("struct ForegroundElement %s = {CENTER_FOCUS, %s};",
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
