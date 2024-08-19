package dev.punchcafe.gbvng.gen.project.assets;

import dev.punchcafe.gbvng.gen.adapter.graphics.IndexArray;
import dev.punchcafe.gbvng.gen.adapter.graphics.PatternBlock;
import dev.punchcafe.gbvng.gen.adapter.banks.BankableAssetBase;
import dev.punchcafe.gbvng.gen.render.banks.AssetVisitor;
import dev.punchcafe.gbvng.gen.shared.SourceName;
import lombok.Builder;
import lombok.Getter;

/**
 * TODO: inlcude in documentation that the Bankable Asset model has the responsibility of
 * defining all it's associated source names, so that any narrative element which relies on it
 * can correctly reference it if it has this model.
 *
 * TODO: make clear distinction of language between Bankable Asset (or asset in general) and Narrative Element
 */
@Builder
public class BackgroundImageAsset extends BankableAssetBase {

    private final SourceName sourceName;
    @Getter private final IndexArray indexArray;
    @Getter private final PatternBlock patternBlock;


    public SourceName getPatternName(){
        return this.sourceName.withSuffix("pattern_block");
    }

    public SourceName getIndexArrayName(){
        return this.sourceName.withSuffix("index_array");
    }

    public SourceName getExternalAssetName(){
        return this.sourceName.withSuffix("external_asset");
    }

    public SourceName getIndexArraySizeMacro(){
        return this.sourceName.withSuffix("pattern_block_size").toUppercase();
    }


    @Override
    public long getSize() {
        // TODO
        return 8_000;
    }

    @Override
    public String getId() {
        // TODO: implement/change to source
        return this.sourceName.toString();
    }

    @Override
    public <T> T acceptVisitor(AssetVisitor<T> visitor) {
        return visitor.visitBackgroundImageAsset(this);
    }
}
