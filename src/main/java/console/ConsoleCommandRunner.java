package console;

import picocli.CommandLine;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleCommandRunner {
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final Scanner scanner = new Scanner(System.in);
    private final CommandLine cmd;

    public ConsoleCommandRunner(Object topLevelCommand) {
        cmd = new CommandLine(topLevelCommand);
        cmd.setExecutionStrategy(new CommandLine.RunLast());
    }

    public void run() {
        String expression;
        while (true) {
            expression = scanner.nextLine();
            String[] exp;
            if (expression.isBlank())
                exp = new String[0];
            else exp = expression.split(" ");

            cmd.execute(exp);
            Object subSubResult = getSubSubObject(cmd);
            Integer subResult = getSubObject(cmd);
            if (subSubResult != null) {
                if (subSubResult instanceof Runnable) {
                    service.submit((Runnable) subSubResult);
                }
            }
            if (subResult != null)
                System.exit(subResult);
        }
    }

    public void addCommand(Object[] commands) {
        for (Object command : commands) {
            cmd.addSubcommand(command);
        }
    }

    private Integer getSubObject(CommandLine cmd) {
        CommandLine.ParseResult parseResult = cmd.getParseResult();
        if (parseResult.subcommand() != null) {
            CommandLine sub = parseResult.subcommand().commandSpec().commandLine();
            return sub.getExecutionResult();
        } else return null;
    }

    private Object getSubSubObject(CommandLine cmd) {
        CommandLine.ParseResult parseResult = cmd.getParseResult();
        if (parseResult.subcommand() != null) {
            if (parseResult.subcommand().subcommand() != null) {
                CommandLine sub = parseResult.subcommand().subcommand().commandSpec().commandLine();
                return sub.getExecutionResult();
            } else return null;
        } else return null;
    }
}
