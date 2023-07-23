package console;

import clients.SimpleClient;
import consoleControl.SimpleClientController;
import consoleControl.SimpleServerController;
import picocli.CommandLine;
import servers.SimpleServer;
import utils.ConnectionUtils;

import java.io.IOException;

@CommandLine.Command(name = "start", aliases = {"-start"})
public class Start implements Runnable {
    private final SimpleServerController serverController = SimpleServerController.getInstance();
    private final SimpleClientController clientController = SimpleClientController.getInstance();

    @Override
    public void run() {
    }

    @CommandLine.Command(name = "server", aliases = {"-server", "-s"})
    SimpleServer startServer(@CommandLine.Option(names = "-p", required = true) int port,
                     @CommandLine.Option(names = "-nc", defaultValue = "1") int maxClient) throws IOException {

        boolean isValidPort = ConnectionUtils.isValidPort(port) && ConnectionUtils.isFreePort(port);
        SimpleServer server = null;
        if (isValidPort) {
            if (maxClient < 1) {
                System.err.printf("Specified max number client %d for server not valid\n", maxClient);
                return null;
            }
            server = serverController.create(port, maxClient);
            System.out.printf("Server %s started on port %d\n", server, (server.getEndpoint().getPort()));
        }
        return server;
    }

    @CommandLine.Command(name = "client", aliases = {"-client", "-c"})
    SimpleClient startClient(@CommandLine.Option(names = "-h", required = true) String host,
                     @CommandLine.Option(names = "-p", required = true) int port) {

        boolean isValidPort = ConnectionUtils.isValidPort(port);
        boolean isValidHost;
        try {
            ConnectionUtils.isValidHost(host);
            isValidHost = ConnectionUtils.isReachedHost(host);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
        ConnectionUtils.isRunServer(host, port);
        SimpleClient client = null;
        if (isValidPort && isValidHost) {
            client = clientController.create(host, port);
            System.out.printf("Client connected to server %s: %d\n",
                                client.getEndpoint().getAddress().getHostAddress(),
                                client.getEndpoint().getPort());
        }
        return client;
    }
}