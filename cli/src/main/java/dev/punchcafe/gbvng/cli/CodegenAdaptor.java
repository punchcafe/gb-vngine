package dev.punchcafe.gbvng.cli;

import dev.punchcafe.vngine.gb.codegen.CodeGenerator;

import java.io.IOException;

public class CodegenAdaptor implements CliProcess {

    private final CodeGenerator codeGenerator = new CodeGenerator();

    @Override
    public void run(String[] args) {
        try {
            this.codeGenerator.run(args);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String invokeWord() {
        return "gen";
    }
}
