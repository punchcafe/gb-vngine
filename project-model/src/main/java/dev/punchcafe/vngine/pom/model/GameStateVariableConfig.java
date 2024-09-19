package dev.punchcafe.vngine.pom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GameStateVariableConfig {
    @JsonProperty("game-state-variables")
    private Map<String, VariableTypes> gameStateVariables;
}
