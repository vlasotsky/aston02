package ru.aston.aston02.repository;

import ru.aston.aston02.model.VinylDisc;

import java.util.List;

public interface VinylDiscRepository {
    void save(VinylDisc disc);

    VinylDisc get(int id);

    void update(int id, VinylDisc disc);

    void delete(int id);

    List<VinylDisc> getAll();
}
