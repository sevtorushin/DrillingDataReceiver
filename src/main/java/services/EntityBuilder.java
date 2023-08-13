package services;

import entities.ParameterEntity;
import entities.SIBParameterEntity;
import entities.WITSParameterEntity;
import entity.Package;
import exceptions.BuildObjectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class EntityBuilder {

    private static final Logger log = LogManager.getLogger(EntityBuilder.class.getSimpleName());

    private ParameterEntity getEntity(Class<? extends Package> packageClass) {
        if (packageClass.getSimpleName().equals("SIBParameter"))
            return new SIBParameterEntity();
        else if (packageClass.getSuperclass().getSimpleName().equals("WITSPackage"))
            return new WITSParameterEntity();
        else {
            log.error("Not valid input argument: " + packageClass);
            throw new IllegalArgumentException("Not valid input argument: " + packageClass);
        }
    }

    public ParameterEntity buildEntity(Package pack) throws BuildObjectException {
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
                log.error("Invalid input argument: " + pack, e);
                throw new BuildObjectException("Invalid input argument: " + pack);
            } catch (IllegalAccessException e) {
                log.error(e);
            }
        }
        return pe;
    }
}
