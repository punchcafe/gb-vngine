package dev.punchcafe.vngine.pom.model;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectObjectModel<N> {
    public static <T> ProjectObjectModel<T> emptyProject(){
        return new ProjectObjectModel<>();
    }

    private GameStateVariableConfig gameStateVariableConfig;
    private List<ChapterConfig> chapterConfigs;
    private Spec spec;
    private List<N> narrativeConfigs;
}
