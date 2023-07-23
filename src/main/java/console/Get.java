package console;

import clients.SimpleClient;
import consoleControl.SimpleClientController;
import consoleControl.SimpleServerController;
import picocli.CommandLine;
import servers.SimpleServer;
import utils.ConnectionUtils;

import java.nio.channels.SocketChannel;
import java.rmi.NoSuchObjectException;
import java.util.List;

@CommandLine.Command(name = "get", aliases = {"-get"})
public class Get implements Runnable {
    private final SimpleServerController serverController = SimpleServerController.getInstance();
    private final SimpleClientController clientController = SimpleClientController.getInstance();

    @Override
    public void run() {
    }

    @CommandLine.Command(name = "server", aliases = {"-server", "-s"})
    void getServer(@CommandLine.Option(names = "-p") int port,
                   @CommandLine.Option(names = {"-all", "all"}, required = true) boolean all) throws NoSuchObjectException {
        ConnectionUtils.isValidPort(port);

        if (port == 0 && all) {
            List<SimpleServer> serverList = serverController.getAllServers();
            if (serverList.isEmpty())
                System.out.println("none");
            else
                serverList.forEach(System.out::println);
        } else if (port != 0 && all) {
            List<SocketChannel> clients = serverController.getAllClients(port);
            if (clients.isEmpty())
                System.out.println("none");
            else
                clients.forEach(System.out::println);
        }
    }

    @CommandLine.Command(name = "client", aliases = {"-client", "-c"})
    void getClient(@CommandLine.Option(names = {"-all", "all"}) boolean all) {
        List<SimpleClient> clients = clientController.getAllClients();
        if (all) {
            if (clients.isEmpty())
                System.out.println("none");
            else
                clients.forEach(System.out::println);
        }
    }
}
