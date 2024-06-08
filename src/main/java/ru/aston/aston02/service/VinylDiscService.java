package ru.aston.aston02.service;

import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.Repository;

import java.util.List;

public class VinylDiscService implements Service {
    private final Repository<Long, VinylDisc> repository;

    public VinylDiscService(Repository<Long, VinylDisc> repository) {
        this.repository = repository;
    }

    public void save(VinylDisc disc) {
        repository.save(disc);
    }

    public VinylDisc get(Long id) {
        return repository.get(id);
    }

    public void update(Long id, VinylDisc disc) {
        repository.update(id, disc);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<VinylDisc> getAll() {
        return repository.getAll();
    }
}
