import connection.SIBReceiverServer;
import connection.WITSReceivingClient;
import entities.SIBParameterEntity;
import entities.WITSParameterEntity;
import entity.SIBParameter;
import entity.WITSPackageTimeBased;
import exceptions.DisconnectedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import service.SIBConverter;
import service.WITSConverter;
import services.SIBParameterRepository;
import services.WITSParameterRepository;
import utils.HibernateUtils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    private static ReentrantLock lock = new ReentrantLock(true);
    private static String path;
    private static String witsServerIP;
    private static int witsServerPort;
    private static boolean isWriteSibData;
    private static boolean isWriteWitsData;

    public static void main(String[] args) {
        initialize();
        Properties props = new Properties();
        props.setProperty("hibernate.connection.url", "jdbc:sqlite:" + path);

        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory(props)) {
            log.debug("SessionFactory is created");
            Runnable storeFromSR = () -> storeSIBData(sessionFactory);
            Runnable storeFromWITS = () -> storeWITSData(sessionFactory);
//            Runnable retrieveFromWITS = () -> retrieveData(sessionFactory);

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            if (isWriteSibData)
                executorService.execute(storeFromSR);
            if (isWriteWitsData)
                executorService.execute(storeFromWITS);
            executorService.shutdown();

            executorService.awaitTermination(10, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void validateSIBData(byte[] data) throws IllegalArgumentException {
        if (data[0] != -56) {
            log.error("Invalid data from SibReceiver client");
            throw new IllegalArgumentException("Invalid data from SibReceiver client");
        }
    }

    private static void validateWITSData(byte[] data) throws IllegalArgumentException {
        if (data[0] != 38) {
            log.error("Invalid data from WITS server");
            throw new IllegalArgumentException("Invalid data from WITS server");
        }
    }

    private static void storeSIBData(SessionFactory sessionFactory) {
        log.debug("SIB data recording started");
        SIBParameterRepository repository = new SIBParameterRepository(sessionFactory);
        SIBConverter converter = new SIBConverter();
        while (true) {
            try (SIBReceiverServer server = new SIBReceiverServer(5111)) {
                log.debug("SibReceiver client is connected on port 5111");
                validateSIBData(server.receiveBytes());
                byte[] data;
                while (true) {
                    data = server.receiveBytes();
                    SIBParameter parameter = converter.convert(data, SIBParameter.class);
                    SIBParameterEntity p1 = new SIBParameterEntity(
                            parameter.getParameterName(), parameter.getParameterData(),
                            parameter.getQuality());
                    lock.lock();
                    repository.save(p1);
                    log.debug("Sib record saved");
                    lock.unlock();
                }
            } catch (DisconnectedException e) {
                log.debug("SibReceiver client is disconnected", e);
            } catch (IOException e) {
                log.error("Sib client input stream read error", e);
            }
        }
    }

    private static void storeWITSData(SessionFactory sessionFactory) {
        log.debug("WITS data recording started");
        WITSParameterRepository repository = new WITSParameterRepository(sessionFactory);
        WITSConverter converter = new WITSConverter();
        try (WITSReceivingClient client = new WITSReceivingClient(witsServerIP, witsServerPort)) {
            log.debug("Client connected to server " + witsServerIP + " on port " + witsServerPort);
            validateWITSData(client.receiveBytes());
            byte[] data;
            while (true) {
                data = client.receiveBytes();
                WITSPackageTimeBased packageTimeBased = (WITSPackageTimeBased) converter
                        .convert(data, WITSPackageTimeBased.class);
                WITSParameterEntity p1 = new WITSParameterEntity(
                        packageTimeBased.getWitsDate(), packageTimeBased.getWitsTime(),
                        packageTimeBased.getBlockPosition(), packageTimeBased.getBitDepth(),
                        packageTimeBased.getDepth(), packageTimeBased.getHookLoad(),
                        packageTimeBased.getPressure());
                lock.lock();
                repository.save(p1);
                log.debug("WITS record saved");
                lock.unlock();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WITS server input stream read error", e);
        }
    }

    public static void retrieveData(SessionFactory sessionFactory) {
        WITSParameterRepository repository = new WITSParameterRepository(sessionFactory);
        List<WITSParameterEntity> entities;
        while (true) {
            try {
                synchronized (Main.class) {
                    entities = repository.getAll();
                }
                System.out.println(entities.size());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initialize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter DB path:");
        path = scanner.nextLine();
        System.out.println("Write sib data? (y/n)");
        isWriteSibData = scanner.nextLine().equals("y");
        System.out.println("Write WITS data? (y/n)");
        isWriteWitsData = scanner.nextLine().equals("y");
        if (isWriteWitsData) {
            System.out.println("Enter WITS server IP:");
            witsServerIP = scanner.nextLine();
            System.out.println("Enter WITS server port:");
            witsServerPort = scanner.nextInt();
        }
        log.debug("DB path: " + path + ", " + "WITS server IP: " + witsServerIP + ", " +
                "WITS server port: " + witsServerPort);
    }
}
