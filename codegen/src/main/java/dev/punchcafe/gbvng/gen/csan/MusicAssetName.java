package dev.punchcafe.gbvng.gen.csan;

public class MusicAssetName {
    public static String getMusicFileDataName(final String source){
        return String.format("%s_Data", source);
    }

    public static String getExternalMusicAssetName(final String source){
        return String.format("external_music_asset_%s", source);
    }
}
