package dev.punchcafe.gbvng.gen.mbanks.utility;

import dev.punchcafe.gbvng.gen.mbanks.assets.TextAsset;
import dev.punchcafe.gbvng.gen.narrative.Narrative;
import dev.punchcafe.gbvng.gen.narrative.Text;
import dev.punchcafe.gbvng.gen.shared.SourceName;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TextAssetExtractor {

    private final ProjectObjectModel<Narrative> narrative;

    public List<TextAsset> extractAllTextAssets(){
        final var allTexts =  this.narrative.getNarrativeConfigs().stream()
                .map(Narrative::getElements)
                .flatMap(List::stream)
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        return convertAndAssignNames(allTexts);
    }

    private List<TextAsset> convertAndAssignNames(final List<Text> texts){
        final var assetArray = new ArrayList<TextAsset>();
        for(int i = 0; i< texts.size(); i++){
            final var text = texts.get(i);
            assetArray.add(TextAsset.builder().text(text.getText()).textAssetSourceName(deriveSourceName(i)).build());
        }
        return assetArray;
    }

    private SourceName deriveSourceName(final int position){
        return SourceName.builder().sourceName(String.format("text_asset_%d", position)).build();
    }
}
