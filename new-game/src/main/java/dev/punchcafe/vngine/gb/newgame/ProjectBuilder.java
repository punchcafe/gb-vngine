package dev.punchcafe.vngine.gb.newgame;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ProjectBuilder {

    private File root;

    public ProjectBuilder(final File root) {
        this.root = root;
    }

    public void generate() throws IOException {
        this.createRootFiles();
        this.createAssetsDir();
        this.createNarrativeDir();
        this.createChapterDir();
    }

    private File createAssetsDir() throws IOException {
        final var assetsDir = createFileWithSubPath("assets");
        FileUtils.copyURLToFile(ClassLoader.getSystemClassLoader().getResource("assets/bkgsample.bkg.asset.png"),
                new File(assetsDir.getPath() + "/bkgsample.bkg.asset.png"));
        return assetsDir;
    }

    private File createNarrativeDir() {
        return createFileWithSubPath("narratives");
    }

    private void createRootFiles() throws IOException {
        FileUtils.copyURLToFile(ClassLoader.getSystemClassLoader().getResource("spec.yml"),
                new File(root.getPath() + "/spec.yml"));
        FileUtils.copyURLToFile(ClassLoader.getSystemClassLoader().getResource("game_state_variables.yml"),
                new File(root.getPath() + "/game_state_variables.yml"));
    }

    private File createChapterDir() {
        return createFileWithSubPath("chapters");
    }

    private File createFileWithSubPath(final String subPath) {
        return new File(root.toString() + "/" + subPath);
    }
}
