package ru.aston.aston02.service;

import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.VinylDiscRepository;

import java.util.List;

// TODO: DTO transformation goes here and then will be given to servlet
public class VinylDiscService {
    private final VinylDiscRepository repository;

    public VinylDiscService(VinylDiscRepository repository) {
        this.repository = repository;
    }

    public void save(VinylDisc disc) {
        repository.save(disc);
    }

    public VinylDisc get(int id) {
        return repository.get(id);
    }

    public void update(int id, VinylDisc disc) {
        repository.update(id, disc);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public List<VinylDisc> getAll() {
        return repository.getAll();
    }
}
