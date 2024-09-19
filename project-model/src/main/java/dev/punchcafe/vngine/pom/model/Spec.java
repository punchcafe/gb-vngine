package dev.punchcafe.vngine.pom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Spec {
    @JsonProperty("starting-chapter")
    private String startingChapter;
}
