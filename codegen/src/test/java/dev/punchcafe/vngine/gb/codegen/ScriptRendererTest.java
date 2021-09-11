package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.render.FixtureRender;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScriptRendererTest {

    //@Test
    void scriptRendererCanResolveDependencies() {
        final var leaf1 = FixtureRender.builder()
                .componentName("leaf1")
                .dependencies(List.of())
                .fixture("\nI am leaf 1!")
                .build();

        final var leaf2 = FixtureRender.builder()
                .componentName("leaf2")
                .dependencies(List.of())
                .fixture("\nI am leaf 2!")
                .build();

        final var leaf3 = FixtureRender.builder()
                .componentName("leaf3")
                .dependencies(List.of())
                .fixture("\nI am leaf 3!")
                .build();

        final var child1 = FixtureRender.builder()
                .componentName("child1")
                .fixture("\nI am child 1")
                .dependencies(List.of("leaf1", "leaf2"))
                .build();

        final var child2 = FixtureRender.builder()
                .componentName("child2")
                .fixture("\nI am child 2")
                .dependencies(List.of())
                .build();

        final var parent1 = FixtureRender.builder()
                .componentName("parent1")
                .fixture("\nI am Parent 1")
                .dependencies(List.of("child1", "child2"))
                .build();

        final var parent2 = FixtureRender.builder()
                .componentName("parent2")
                .fixture("\nI am Parent 2")
                .dependencies(List.of("leaf 3"))
                .build();

        final var parent3 = FixtureRender.builder()
                .componentName("parent3")
                .fixture("\nI am Parent 3")
                .dependencies(List.of())
                .build();

        final var unitUnderTest = ScriptRenderer.builder()
                .componentRenderer(parent3)
                .componentRenderer(leaf3)
                .componentRenderer(parent2)
                .componentRenderer(child2)
                .componentRenderer(leaf2)
                .componentRenderer(parent1)
                .componentRenderer(child1)
                .componentRenderer(leaf1)
                .build();

        final var expectedResult = "\n" +
                "I am leaf 1!\n" +
                "I am leaf 2!\n" +
                "I am child 1\n" +
                "I am child 2\n" +
                "I am leaf 3!\n" +
                "I am Parent 1\n" +
                "I am Parent 2\n" +
                "I am Parent 3";

        assertEquals(unitUnderTest.render(), expectedResult);
    }
}
