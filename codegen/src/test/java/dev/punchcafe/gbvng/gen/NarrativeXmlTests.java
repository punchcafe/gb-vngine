package dev.punchcafe.gbvng.gen;

import dev.punchcafe.gbvng.gen.narrative.Narrative;
import org.junit.jupiter.api.Test;
import org.simpleframework.xml.core.Persister;

import java.io.File;

public class NarrativeXmlTests {

    @Test
    void narrativeXmlTestHarness() throws Exception {
        final var serializer = new Persister();
        File source = new File("src/test/resources/sample-project/narratives/nar_01.vnx");
        Narrative narrative = serializer.read(Narrative.class, source);
        System.out.println("stop");
    }
}
