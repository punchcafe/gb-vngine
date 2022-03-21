package dev.punchcafe.gbvng.gen.mbanks.assets;

import dev.punchcafe.gbvng.gen.mbanks.BankableAssetBase;
import dev.punchcafe.gbvng.gen.mbanks.renderers.AssetVisitor;
import dev.punchcafe.gbvng.gen.shared.SourceName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class TextAsset extends BankableAssetBase {
    // TODO: implement char validation? this is dependant on the config.
    private final SourceName textAssetSourceName;
    private final String text;

    @Override
    public long getSize() {
        return text.length() * 8;
    }

    @Override
    public String getId() {
        // TODO: replace for SourceName
        return null;
    }

    public SourceName getSourceName() {
        return this.textAssetSourceName;
    }
    public SourceName getExternalAssetSourceName() {
        return this.textAssetSourceName.withPrefix("external_asset");
    }

    @Override
    public <T> T acceptVisitor(AssetVisitor<T> visitor) {
        return visitor.visitTextAsset(this);
    }
}
