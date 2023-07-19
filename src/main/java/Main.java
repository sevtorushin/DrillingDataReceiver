import check.PackageValidator;
import clients.SimpleClient;
import consoleControl.Operation;
import dao.DaoImpl;
import entities.ParameterEntity;
import entities.SIBParameterEntity;
import entities.WITSParameterEntity;
import entity.Cached;
import entity.Package;
import exceptions.BuildObjectException;
import org.hibernate.SessionFactory;
import servers.SimpleServer;
import service.Convertable;
import services.SIBParameterRepository;
import services.WITSParameterRepository;
import utils.HibernateUtils;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static ReentrantLock lock = new ReentrantLock(true);
    private static String path;
    static ExecutorService service = Executors.newCachedThreadPool();
    static SessionFactory sessionFactory;
    static SIBParameterRepository sibRepo;
    static WITSParameterRepository witsRepo;
    static DaoImpl<ParameterEntity> repo;

    //---------------------------
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
//        path = getPath(args[0]);
//        props.setProperty("hibernate.connection.url", "jdbc:sqlite:" + path);
        sessionFactory = HibernateUtils.getSessionFactory(props);
        sibRepo = new SIBParameterRepository(sessionFactory);
        witsRepo = new WITSParameterRepository(sessionFactory);

//        --------------------------------------------------
        Operation op = new Operation();
        new Thread(op).start();

        Callable<Cached> callableServers = () -> (SimpleServer) getNewParticipant(op.getServers());
        Callable<Cached> callableClients = () -> (SimpleClient) getNewParticipant(op.getClients());
        List<Callable<Cached>> callableList = List.of(callableServers, callableClients);

        while (true) {
            Cached part = service.invokeAny(callableList);
            Runnable task = () -> readCache(part);
            service.submit(task);
        }
    }

    private static String getPath(String arg) {
        if (Files.exists(Paths.get(arg)))
        return Paths.get(arg).toString();
        else throw new IllegalArgumentException("Wrong path to database: " + arg);
    }

    private static void readCache(Cached part) {
        System.out.println("Start reading cache for " + part);
        byte[] data;
        while (!isStopped(part)) {
            data = part.readAllCache();
            try {
                if (data.length == 0) {
                    Thread.sleep(500);
                    continue;
                }
                try {
                    lock.lock();
                    save(data);
                } catch (BuildObjectException e){
                    System.err.println("Illegal data");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Reading cache stopped for " + part);
    }

    private static void save(byte[] data) throws BuildObjectException {
        Class<? extends Package> clazz = PackageValidator.identify(data);
        Convertable<Package> converter = (Convertable<Package>) PackageValidator.getPackageConverter(clazz);
        Package p = converter.convert(data, clazz);
        ParameterEntity entity = buildEntity(p);
        if (entity instanceof SIBParameterEntity) {
            sibRepo.save((SIBParameterEntity) entity);
        } else  if (entity instanceof WITSParameterEntity)
            witsRepo.save((WITSParameterEntity) entity);
    }

    private static boolean isStopped(Cached part) {
        if (part instanceof SimpleServer)
            return ((SimpleServer) part).isStopped();
        else return ((SimpleClient) part).isStopped();
    }

    private static Object getNewParticipant(LinkedBlockingQueue<?> participants) {
        LinkedBlockingQueue<Object> list = new LinkedBlockingQueue<>(participants);
        Object participant = null;
        while (participant == null) {
            try {
                participant = participants.stream()
                        .filter(part -> !list.contains(part))
                        .findFirst()
                        .orElse(null);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return null;
            }
        }
        return participant;
    }

    private static ParameterEntity getEntity(Class<? extends Package> packageClass){
        if (packageClass.getSimpleName().equals("SIBParameter"))
            return new SIBParameterEntity();
        else if (packageClass.getSuperclass().getSimpleName().equals("WITSPackage"))
            return new WITSParameterEntity();
        else throw new IllegalArgumentException("Not valid input argument: " + packageClass);
    }

    private static ParameterEntity buildEntity(Package pack) throws BuildObjectException {
        Class<? extends Package> packageClazz = pack.getClass();
        ParameterEntity pe = getEntity(packageClazz);
        Class<? extends ParameterEntity> parameterClazz = pe.getClass();
        Field[] packageF = packageClazz.getDeclaredFields();
        for (Field f : packageF) {
            f.setAccessible(true);
            String fieldName = f.getName();
            Field ef;
            try {
                ef = parameterClazz.getDeclaredField(fieldName);
                ef.setAccessible(true);
                ef.set(pe, f.get(pack));
            } catch (NoSuchFieldException e) {
                throw new BuildObjectException("Invalid input argument: " + pack);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return pe;
    }

    private static DaoImpl<? extends ParameterEntity> getRepo(ParameterEntity entity){
        if (entity instanceof SIBParameterEntity) {
            if (sibRepo == null)
                return new SIBParameterRepository(sessionFactory);
            else return sibRepo;
        } else if (entity instanceof WITSParameterEntity) {
            if (witsRepo == null)
                return new WITSParameterRepository(sessionFactory);
            else return witsRepo;
        } else throw new IllegalArgumentException("Not valid input argument: " + entity);
    }
}
