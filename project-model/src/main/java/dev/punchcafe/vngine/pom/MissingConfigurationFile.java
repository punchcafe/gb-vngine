package dev.punchcafe.vngine.pom;

public class MissingConfigurationFile extends RuntimeException {
    public MissingConfigurationFile(final String missingFileName){
        super(String.format("Missing project configuration file: %s", missingFileName));
    }
}
