package dev.punchcafe.gbvng.gen.project.config;

import lombok.Data;
import org.simpleframework.xml.Text;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PortraitSetConfig {
    @Text
    private String portraitSet;

    public List<String> allFilesInSet(){
        if(portraitSet == null || portraitSet.isBlank()){
            return List.of();
        }
        return Stream.of(portraitSet.split("\n"))
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .collect(Collectors.toList());
    }
}
