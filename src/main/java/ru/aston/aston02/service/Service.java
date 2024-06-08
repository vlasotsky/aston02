package ru.aston.aston02.service;

import ru.aston.aston02.model.VinylDisc;

import java.util.List;

public interface Service {

    void save(VinylDisc disc);

    VinylDisc get(Long id);

    void update(Long id, VinylDisc disc);

    void delete(Long id);

    List<VinylDisc> getAll();
}
