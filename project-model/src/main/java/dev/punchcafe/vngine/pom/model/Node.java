package dev.punchcafe.vngine.pom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class Node {
    private String id;
    private NodeType type;
    @JsonProperty("narrative-id")
    private String narrativeId;
    @JsonProperty("game-state-modifiers")
    private List<String> gameStateModifiers;
    private List<Branch> branches;
}
