package dev.punchcafe.gbvng.gen.render.banks;

import dev.punchcafe.gbvng.gen.project.assets.BackgroundImageAsset;
import dev.punchcafe.gbvng.gen.project.assets.BackgroundMusicAsset;
import dev.punchcafe.gbvng.gen.project.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.project.assets.TextAsset;

public interface AssetVisitor<T> {

    T visitForegroundAsset(final ForegroundAssetSet set);
    T visitBackgroundMusicAsset(final BackgroundMusicAsset asset);
    T visitTextAsset(final TextAsset asset);
    T visitBackgroundImageAsset(final BackgroundImageAsset asset);
}
