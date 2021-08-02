package dev.punchcafe.vngine.gb.codegen.narrative;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SimpleNarrative implements NarrativeElement {
    private String id;
    private List<String> messages;
}
