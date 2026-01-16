package edu.kis.powp.jobs2d.command.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.OperateToCommand;
import edu.kis.powp.jobs2d.command.SetPositionCommand;

public class JsonCommandImportParser implements CommandImportParser {
    private static final String COMMANDS_KEY = "commands";
    private static final String NAME_KEY = "name";
    private static final String TYPE_KEY = "type";
    private static final String X_KEY = "x";
    private static final String Y_KEY = "y";
    private static final String TYPE_SET_POSITION = "setPosition";
    private static final String TYPE_OPERATE_TO = "operateTo";

    @Override
    public CommandImportResult parse(String text) {
        Object root = parseJson(text);
        if (root instanceof List) {
            return new CommandImportResult(parseCommandList((List<?>) root), null);
        }
        if (root instanceof Map) {
            Map<?, ?> rootMap = (Map<?, ?>) root;
            Object commandsObj = rootMap.get(COMMANDS_KEY);
            if (commandsObj == null) {
                throw new CommandImportException("Missing 'commands' field");
            }
            String name = readOptionalString(rootMap.get(NAME_KEY));
            return new CommandImportResult(parseCommandList(asList(commandsObj)), name);
        }
        throw new CommandImportException("Unsupported JSON root");
    }

    private Object parseJson(String text) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        if (engine == null) {
            throw new CommandImportException("JavaScript engine not available");
        }
        engine.put("jsonText", text);
        try {
            return engine.eval("Java.asJSONCompatible(JSON.parse(jsonText))");
        } catch (ScriptException e) {
            throw new CommandImportException("Invalid JSON", e);
        }
    }

    private List<DriverCommand> parseCommandList(List<?> rawCommands) {
        List<DriverCommand> commands = new ArrayList<>();
        for (Object entry : rawCommands) {
            commands.add(parseCommand(entry));
        }
        return commands;
    }

    private DriverCommand parseCommand(Object entry) {
        if (!(entry instanceof Map)) {
            throw new CommandImportException("Command entry must be an object");
        }
        Map<?, ?> command = (Map<?, ?>) entry;
        String type = readRequiredString(command.get(TYPE_KEY), TYPE_KEY);
        if (TYPE_SET_POSITION.equals(type)) {
            int x = readRequiredInt(command.get(X_KEY), X_KEY);
            int y = readRequiredInt(command.get(Y_KEY), Y_KEY);
            return new SetPositionCommand(x, y);
        }
        if (TYPE_OPERATE_TO.equals(type)) {
            int x = readRequiredInt(command.get(X_KEY), X_KEY);
            int y = readRequiredInt(command.get(Y_KEY), Y_KEY);
            return new OperateToCommand(x, y);
        }
        throw new CommandImportException("Unknown command type: " + type);
    }

    private static List<?> asList(Object value) {
        if (value instanceof List) {
            return (List<?>) value;
        }
        throw new CommandImportException("'commands' must be an array");
    }

    private static String readRequiredString(Object value, String field) {
        if (value instanceof String) {
            return (String) value;
        }
        throw new CommandImportException("Field '" + field + "' must be a string");
    }

    private static String readOptionalString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        throw new CommandImportException("Field '" + NAME_KEY + "' must be a string");
    }

    private static int readRequiredInt(Object value, String field) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                throw new CommandImportException("Field '" + field + "' must be an integer", e);
            }
        }
        throw new CommandImportException("Field '" + field + "' must be an integer");
    }
}
