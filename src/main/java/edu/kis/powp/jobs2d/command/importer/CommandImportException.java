package edu.kis.powp.jobs2d.command.importer;

public class CommandImportException extends RuntimeException {
    public CommandImportException(String message) {
        super(message);
    }

    public CommandImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
