package dev.punchcafe.gbvng.gen.config;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import java.util.List;

@Data
public class PortraitSetConfig {
    @Text
    private String portraitSet;

    public List<String> allFilesInSet(){
        return List.of(portraitSet.split("\n"));
    }
}
