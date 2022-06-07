package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundImageAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundMusicAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.mbanks.assets.TextAsset;

public interface AssetVisitor<T> {

    T visitForegroundAsset(final ForegroundAssetSet set);
    T visitBackgroundMusicAsset(final BackgroundMusicAsset asset);
    T visitTextAsset(final TextAsset asset);
    T visitBackgroundImageAsset(final BackgroundImageAsset asset);
}
