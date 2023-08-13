package services;

import check.PackageValidator;
import clients.simple.SimpleClient;
import entities.ParameterEntity;
import entities.SIBParameterEntity;
import entities.WITSParameterEntity;
import entity.Cached;
import entity.Package;
import exceptions.BuildObjectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import servers.simple.SimpleServer;
import service.Convertable;

import java.util.concurrent.locks.ReentrantLock;

public class DataBaseSaver {
    private final ReentrantLock lock = new ReentrantLock(true);
    private final EntityBuilder builder = new EntityBuilder();
    private final SIBParameterRepository sibRepo;
    private final WITSParameterRepository witsRepo;

    private static final Logger log = LogManager.getLogger(DataBaseSaver.class.getSimpleName());

    public DataBaseSaver(SessionFactory sessionFactory) {
        this.sibRepo = new SIBParameterRepository(sessionFactory);
        this.witsRepo = new WITSParameterRepository(sessionFactory);
    }

    public void storeToDB(Cached part) {
        log.info("Start stored cache for " + part);
        byte[] data;
        while (!isStopped(part)) {
            data = part.readAllCache();
            try {
                if (data.length == 0) {
                    Thread.sleep(500);
                    continue;
                }
                log.debug("Data received from " + part);
                try {
                    lock.lock();
                    save(data);
                    log.info("Data saved from " + part);
                } catch (BuildObjectException e) {
                    log.warn("Illegal data", e);
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                log.debug("Store interrupted", e);
            }
        }
        log.info("Stored cache stopped for " + part);
    }

    private void save(byte[] data) throws BuildObjectException {
        Class<? extends Package> clazz = PackageValidator.identify(data);
        Convertable<Package> converter = (Convertable<Package>) PackageValidator.getPackageConverter(clazz);
        Package p = converter.convert(data, clazz);
        ParameterEntity entity = builder.buildEntity(p);
        if (entity instanceof SIBParameterEntity) {
            sibRepo.save((SIBParameterEntity) entity);
        } else if (entity instanceof WITSParameterEntity)
            witsRepo.save((WITSParameterEntity) entity);
    }

    private static boolean isStopped(Cached part) {
        if (part instanceof SimpleServer)
            return ((SimpleServer) part).isStopped();
        else return ((SimpleClient) part).isStopped();
    }
}
