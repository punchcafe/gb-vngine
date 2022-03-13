package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundMusic;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;

public interface AssetVisitor<T> {

    T visitForegroundAsset(final ForegroundAssetSet set);
    T visitBackgroundMusicAsset(final BackgroundMusic asset);
}
