package console;

import consoleControl.SimpleServerController;
import picocli.CommandLine;
import utils.ConnectionUtils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

@CommandLine.Command(name = "remove", aliases = {"-remove"})
public class Remove implements Runnable {
    private final SimpleServerController serverController = SimpleServerController.getInstance();

    @Override
    public void run() {
    }

    @CommandLine.Command(name = "server", aliases = {"-server", "-s"})
    void removeClients(@CommandLine.Option(names = "-p", required = true) int serverPort,
                       @CommandLine.Option(names = {"-all", "all"}) boolean all,
                       @CommandLine.Option(names = "-cp") int clientPort) {
        ConnectionUtils.isValidPort(serverPort);
        ConnectionUtils.isValidPort(clientPort);

        if (!all && clientPort != 0) {
            try {
                SocketChannel channel = serverController.removeClient(serverPort, clientPort);
                System.out.printf("Client %s: %d have been disconnected\n",
                        channel.socket().getInetAddress().getHostAddress(), channel.socket().getPort());
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        } else if (all && clientPort == 0) {
            try {
                List<SocketChannel> channelList = serverController.removeAllClients(serverPort);
                System.out.println("These clients have been disconnected:");
                channelList.forEach(socketChannel -> System.out.println(
                        socketChannel.socket().getInetAddress().getHostAddress() + ": " + socketChannel.socket().getPort()));
            } catch (IOException e) {
                e.printStackTrace(); //todo логировать
            }
        }
    }
}
