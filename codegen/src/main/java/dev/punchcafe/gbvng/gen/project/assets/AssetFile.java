package dev.punchcafe.gbvng.gen.project.assets;

import lombok.Builder;
import lombok.Getter;

import java.io.File;

@Builder
@Getter
public class AssetFile {
    private final String assetName;
    private final AssetType type;
    private final File file;
}
