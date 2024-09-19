package dev.punchcafe.vngine.pom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ChapterConfig {
    @JsonProperty("chapter-id")
    private String chapterId;
    private String firstNodeId;
    private List<Node> nodes;
    @JsonProperty("chapter-variables")
    private Map<String, VariableTypes> chapterVariables;
}
