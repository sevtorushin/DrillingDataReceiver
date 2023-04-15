package dao;

import org.sqlite.SQLiteException;

import java.util.List;

public interface Dao<T> {
    void save(T t) throws SQLiteException;
    T get(Long id);
    List<T> getAll();
}
