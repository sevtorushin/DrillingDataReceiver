package utils;

import entities.SIBParameterEntity;
import entities.WITSParameterEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    private HibernateUtils() {
    }

    public static SessionFactory getSessionFactory(Properties properties){
        Configuration cfg = new Configuration();
        cfg.addProperties(properties);
        if (sessionFactory==null) {
            sessionFactory = cfg
                    .addAnnotatedClass(SIBParameterEntity.class)
                    .addAnnotatedClass(WITSParameterEntity.class)
                    .buildSessionFactory();
            return sessionFactory;
        } else return sessionFactory;
    }
}
