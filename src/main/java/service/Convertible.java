package service;

public interface Convertible<T> {
    T convert(byte[] data, Class<? extends T> clazz);
}
