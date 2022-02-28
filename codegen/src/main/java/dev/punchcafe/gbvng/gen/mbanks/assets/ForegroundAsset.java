package dev.punchcafe.gbvng.gen.mbanks.assets;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ForegroundAsset {

    private static final int INTEGER_SIZE_BYTES = 2;

    private String cVariableName;
    private List<Integer> patternTableIndexArray;

    public int sizeInBytes(){
        return patternTableIndexArray.size() * INTEGER_SIZE_BYTES;
    }
}
