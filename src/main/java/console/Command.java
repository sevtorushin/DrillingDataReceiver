package console;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Command {
    private String commandName;
    private Map<String, Object> options;
    private Set<Command> subCommands;

    public Command() {
        options = new HashMap<>();
        subCommands = new HashSet<>();
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setOptions(String name, Object value){
        options.put(name, value);
    }

    public void setSubCommand(Command command){
        subCommands.add(command);
    }
}
