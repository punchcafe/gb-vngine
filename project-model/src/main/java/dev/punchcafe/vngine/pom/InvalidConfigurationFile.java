package dev.punchcafe.vngine.pom;

public class InvalidConfigurationFile extends RuntimeException {
    public InvalidConfigurationFile(final String missingFileName){
        super(String.format("Couldn't parse configuration file: %s", missingFileName));
    }
}
