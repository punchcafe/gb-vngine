package dev.punchcafe.vngine.pom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Branch {

    @JsonProperty("node-id")
    private String nodeId;
    // Use for either automatic or player based since it's just a yaml DTO
    @JsonProperty("predicate-expression")
    private String predicateExpression;
    private String prompt;
}
