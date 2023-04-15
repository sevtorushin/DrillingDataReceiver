package dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

import java.util.List;

public abstract class DaoImpl<T> implements Dao<T> {
    private final SessionFactory factory;
    private static final Logger log = LogManager.getLogger(DaoImpl.class.getSimpleName());

    public DaoImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void save(T t) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(t);
            transaction.commit();
        } catch (TransactionException e) {
            if (transaction != null) {
                transaction.rollback();
                log.debug("Concurrent access. Transaction rollback");
            }
            this.save(t); //TODO Может зациклиться при долгой блокировки БД. Переделать с использованием счетчика рекурсивных вызовов (например не более 10 попыток рестарта)
            log.debug("Transaction restart successful");
        }
    }

    @Override
    public T get(Long id) {
        T t = null;
        try (Session session = factory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            t = session.get(getType(), id);
            transaction.commit();
        }
        return t;
    }

    @Override
    public List<T> getAll() {
        List<T> tList = null;
        try (Session session = factory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            tList = session.createQuery("from " + getType().getSimpleName()).list();
            transaction.commit();
        }
        return tList;
    }

    public abstract Class<T> getType();
}
