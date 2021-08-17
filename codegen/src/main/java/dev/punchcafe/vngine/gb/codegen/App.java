/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.narrative.NarrativeReader;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.pom.PomLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

public class App {

    public static void main(String[] args) {}

    public void run(final File vngProjectRoot, final String scriptDestination) throws IOException, IllegalAccessException, InvocationTargetException {
        final var narrativeReader = new NarrativeReader();

        final var assetsDirectory = vngProjectRoot.listFiles((root, fileName) -> fileName.equals("assets"))[0];

        final var gameConfig = PomLoader.forGame(vngProjectRoot, narrativeReader).loadGameConfiguration();
        final var rendererFactory = new RendererFactory(gameConfig, assetsDirectory);


        final var allComponents = Arrays.stream(rendererFactory.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RendererSupplier.class))
                .map(supplierMethod -> getFromMethod(supplierMethod, rendererFactory))
                .collect(toList());

        final var scriptRenderer = ScriptRenderer.builder()
                .componentRenderers(allComponents)
                .build();

        final var renderedScript = scriptRenderer.render();
        final var out = new BufferedWriter(new FileWriter(scriptDestination));
        out.write(renderedScript);
        out.close();
    }

    private ComponentRenderer getFromMethod(final Method method, final RendererFactory factory){
        try {
            return (ComponentRenderer) method.invoke(factory);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
