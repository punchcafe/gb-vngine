package dev.punchcafe.gbvng.cli;

import dev.punchcafe.vngine.gb.newgame.NewProjectGenerator;

public class NewGameAdaptor implements CliProcess {

    private final NewProjectGenerator newProjectGenerator = new NewProjectGenerator();

    @Override
    public void run(String[] args) {
        newProjectGenerator.run(args);
    }

    @Override
    public String invokeWord() {
        return "new";
    }
}
