import clients.simple.SimpleClient;
import clients.simple.SimpleClientController;
import consoleControl.ConsoleCommandRunner;
import consoleControl.TopLevelCommand;
import entity.Cached;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import servers.simple.SimpleServer;
import servers.simple.SimpleServerController;
import services.DataBaseSaver;
import utils.HibernateUtils;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static String path;
    static ExecutorService service = Executors.newCachedThreadPool();
    static SessionFactory sessionFactory;
    static DataBaseSaver dbSaver;

    private static final Logger log = LogManager.getLogger(Main.class.getSimpleName());

    //---------------------------
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        path = args[0];
        if (path == null) {
            log.error("Invalid database path");
            return;
        }
        props.setProperty("hibernate.connection.url", "jdbc:sqlite:" + path);
        sessionFactory = HibernateUtils.getSessionFactory(props);
        dbSaver = new DataBaseSaver(sessionFactory);

        ConsoleCommandRunner runner = new ConsoleCommandRunner(new TopLevelCommand());
        new Thread(runner).start();

        SimpleServerController simpleServerController = SimpleServerController.getInstance();
        SimpleClientController clientController = SimpleClientController.getInstance();

        Callable<Cached> callableServers = () -> {
            SimpleServer server = null;
            while (server == null) {
                server = simpleServerController.getNewServer();
                Thread.sleep(100);
            }
            log.debug("New server received");
            return server;
        };

        Callable<Cached> callableClients = () -> {
            SimpleClient client = null;
            while (client == null) {
                client = clientController.getNewClient();
                Thread.sleep(100);
            }
            log.debug("New client received");
            return client;
        };

        List<Callable<Cached>> callableList = List.of(callableServers, callableClients);

        while (true) {
            Cached part = service.invokeAny(callableList);
            Runnable task = () -> dbSaver.storeToDB(part);
            service.submit(task);
            log.info("Monitoring for " + part + " started");
        }
    }
}
