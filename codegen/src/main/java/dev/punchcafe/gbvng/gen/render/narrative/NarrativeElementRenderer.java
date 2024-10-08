package dev.punchcafe.gbvng.gen.render.narrative;

import dev.punchcafe.gbvng.gen.adapter.MusicAssetName;
import dev.punchcafe.gbvng.gen.adapter.NarrativeName;
import dev.punchcafe.gbvng.gen.adapter.PortraitAssetNameVariableSanitiser;
import dev.punchcafe.gbvng.gen.adapter.assets.TextAsset;
import dev.punchcafe.gbvng.gen.project.narrative.*;
import lombok.Builder;

import java.util.Map;

@Builder
public class NarrativeElementRenderer implements NarrativeElementVisitor<String> {

    private final int index;
    private final String narrativeId;
    private final Map<String, TextAsset> textAssetCache;

    @Override
    public String visitClearText(final ClearText clearText) {
        return String.format("struct NarrativeElement %s = {0x00, CLEAR_TEXT};",
                NarrativeName.elementName(narrativeId, index));
    }

    @Override
    public String visitClearForeground(final ClearForeground clearText) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visitSetForeground(final SetForeground setForeground) {
        final var body = renderSetForegroundBody(setForeground);
        final String narrativeElement = narrativeElementForType("FOREGROUND");
        return body + "\n" + narrativeElement;
    }

    @Override
    public String visitSetBackground(final SetBackground setBackground) {
        return String.format("struct NarrativeElement %s = {&%s, %s};",
                NarrativeName.elementName(narrativeId, index),
                //TODO: have this use a Background Asset Cache to get the external soruce name
                setBackground.getSrc() + "_external_asset",
                "BACKGROUND");
    }

    public static String TILE_ASSIGNMENT_SUFFIX = "_tile_assign";
    public static String TILE_DATA_SUFFIX = "_tile_data";
    public static String TILE_DATA_SIZE_DEFINITION_SUFFIX = "_TILE_DATA_SIZE";


    private String renderSetForegroundBody(final SetForeground setForeground) {
        switch (setForeground.getAlignment().toLowerCase()) {
            case "left":
                return String.format("struct ForegroundElement %s = {LEFT_PORTRAIT, &%s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        PortraitAssetNameVariableSanitiser.getForegroundAssetName(setForeground.getSrc()));
            case "right":
                return String.format("struct ForegroundElement %s = {RIGHT_PORTRAIT, &%s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        PortraitAssetNameVariableSanitiser.getForegroundAssetName(setForeground.getSrc()));
            case "center":
                return String.format("struct ForegroundElement %s = {CENTER_PORTRAIT, &%s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        PortraitAssetNameVariableSanitiser.getForegroundAssetName(setForeground.getSrc()));
            case "center_focus":
                return String.format("struct ForegroundElement %s = {CENTER_FOCUS, &%s};",
                        NarrativeName.elementBodyName(narrativeId, index),
                        PortraitAssetNameVariableSanitiser.getForegroundAssetName(setForeground.getSrc()));
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitDelay(final Delay delay) {
        final var body = String.format("struct Pause %s = {%d};",
                NarrativeName.elementBodyName(narrativeId, index),
                delay.getSeconds());
        final String narrativeElement = narrativeElementForType("DELAY");
        return body + "\n" + narrativeElement;
    }


    @Override
    public String visitText(final Text text) {
        final var externalAssetName = this.textAssetCache.get(text.getText()).getExternalAssetSourceName().toString();
        return String.format("struct NarrativeElement %s = {&%s, TEXT};",
        NarrativeName.elementName(narrativeId, index), externalAssetName);
    }

    @Override
    public String visitPlayMusic(final PlayMusic playMusic) {
        final var assetName = MusicAssetName.getExternalMusicAssetName(playMusic.getSource());
        return String.format("struct NarrativeElement %s = {&%s, %s};", NarrativeName.elementName(narrativeId, index),
                assetName,
                "PLAY_MUSIC");

    }

    private String narrativeElementForType(final String cEnumType) {
        return String.format("struct NarrativeElement %s = {&%s, %s};",
                NarrativeName.elementName(narrativeId, index),
                NarrativeName.elementBodyName(narrativeId, index),
                cEnumType);
    }
}
