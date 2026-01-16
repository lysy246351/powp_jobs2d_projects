package edu.kis.powp.jobs2d.command.importer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kis.powp.jobs2d.command.DriverCommand;

public class CommandImportResult {
    private static final String DEFAULT_NAME = "Imported Command";

    private final List<DriverCommand> commands;
    private final String name;

    public CommandImportResult(List<DriverCommand> commands, String name) {
        if (commands == null) {
            throw new IllegalArgumentException("commands cannot be null");
        }
        this.commands = Collections.unmodifiableList(new ArrayList<>(commands));
        this.name = normalizeName(name);
    }

    public List<DriverCommand> getCommands() {
        return commands;
    }

    public String getName() {
        return name;
    }

    private static String normalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return DEFAULT_NAME;
        }
        return name;
    }
}
