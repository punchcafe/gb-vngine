package dev.punchcafe.gbvng.cli;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class DelegatorProcess implements CliProcess {

    private final Map<String, CliProcess> delegateProcesses;
    private final String invokeWord;

    public DelegatorProcess(final String name, final List<CliProcess> processes){
        this.invokeWord = name;
        this.delegateProcesses = processes.stream()
                .collect(toMap(CliProcess::invokeWord, identity()));
    }

    @Override
    public void run(String[] args) {
        delegateProcesses.get(args[0]).run(arrayWithoutFirstElement(args));
    }

    private String[] arrayWithoutFirstElement(String[] array){
        if (array.length <= 1){
            return null;
        }
        return Arrays.copyOfRange(array, 1, array.length);
    }

    @Override
    public String invokeWord() {
        return this.invokeWord;
    }
}
