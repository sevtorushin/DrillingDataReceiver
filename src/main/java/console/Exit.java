package console;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "exit", aliases = {"-exit", "quit", "-quit"})
public class Exit implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
//        System.exit(0);
        return 0;
    }
}
