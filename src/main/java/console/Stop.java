package console;

import clients.SimpleClient;
import consoleControl.SimpleClientController;
import consoleControl.SimpleServerController;
import picocli.CommandLine;
import servers.SimpleServer;
import utils.ConnectionUtils;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(name = "stop", aliases = {"-stop"})
public class Stop implements Runnable {

    private final SimpleServerController serverController = SimpleServerController.getInstance();
    private final SimpleClientController clientController = SimpleClientController.getInstance();

    @Override
    public void run() {

    }

    @CommandLine.Command(name = "server", aliases = {"-server", "-s"})
    void stopServer(@CommandLine.Option(names = "-p") int port,
                    @CommandLine.Option(names = {"-all", "all"}) boolean all) {
        ConnectionUtils.isValidPort(port);

        if (port == 0 && all) {
            try {
                List<SimpleServer> serverList = serverController.stopAllServers();
                System.out.println("These servers have been stopped:");
                serverList.forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        } else if (!all && port != 0) {
            try {
                SimpleServer server = serverController.stop(port);
                System.out.printf("Server %s have been stopped\n", server);
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        }
    }

    @CommandLine.Command(name = "client", aliases = {"-client", "-c"})
    void stopClient(@CommandLine.Option(names = "-p") int port,
                    @CommandLine.Option(names = {"-all", "all"}) boolean all) {
        ConnectionUtils.isValidPort(port);

        if (all && port == 0) {
            try {
                List<SimpleClient> clientList = clientController.stopAllClients();
                System.out.println("These clients have been disconnected:");
                clientList.forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        } else if (port != 0) {
            try {
                SimpleClient client = clientController.stop(port);
                System.out.printf("Client %s have been disconnected\n", client);
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        }
    }
}