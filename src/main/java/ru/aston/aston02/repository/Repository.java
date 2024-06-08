package ru.aston.aston02.repository;

import ru.aston.aston02.model.VinylDisc;

import java.util.List;

public interface Repository<K, T> {
    void save(T disc);

    VinylDisc get(K id);

    void update(K id, T disc);

    void delete(K id);

    List<T> getAll();
}
