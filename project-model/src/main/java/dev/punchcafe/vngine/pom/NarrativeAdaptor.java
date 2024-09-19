package dev.punchcafe.vngine.pom;

import java.io.File;
import java.util.List;

public interface NarrativeAdaptor<N> {
    List<N> parseNarrativesFromDirectory(File narrativesDirectory);
}
