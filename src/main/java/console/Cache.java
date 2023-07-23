package console;

import consoleControl.SimpleClientController;
import consoleControl.SimpleServerController;
import picocli.CommandLine;
import utils.ConnectionUtils;

import java.io.IOException;

@CommandLine.Command(name = "cache", aliases = {"-cache", "ch", "-ch"})
public class Cache implements Runnable {
    private final SimpleServerController serverController = SimpleServerController.getInstance();
    private final SimpleClientController clientController = SimpleClientController.getInstance();

    @Override
    public void run() {
    }

    @CommandLine.Command(name = "server", aliases = {"-server", "-s"})
    Runnable cacheServer(@CommandLine.Option(names = "-p", required = true) int port) {
        ConnectionUtils.isValidPort(port);

        return () -> {
            try {
                System.out.printf("Caching for server %d started\n", port);
                serverController.read(port);
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        };
    }

    @CommandLine.Command(name = "client", aliases = {"-client", "-c"})
    Runnable cacheClient(@CommandLine.Option(names = "-h", required = true) String serverHost,
                     @CommandLine.Option(names = "-p", required = true) int port) throws IOException {
        ConnectionUtils.isValidPort(port);
        ConnectionUtils.isValidHost(serverHost);
        ConnectionUtils.isReachedHost(serverHost);
        ConnectionUtils.isRunServer(serverHost, port);
       return  () -> {
            try {
                System.out.printf("Caching for client %s: %d started\n",serverHost, port);
                clientController.read(serverHost, port);
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        };
    }
}
