package ru.aston.aston02.service;

import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.Repository;

import java.util.List;

public class VinylDiscServiceImpl implements VinylDiscService, VinylDiscServiceFactory<Long, VinylDisc> {
    private final Repository<Long, VinylDisc> repository;

    public VinylDiscServiceImpl(Repository<Long, VinylDisc> repository) {
        this.repository = repository;
    }

    public void saveVinylDisc(VinylDisc disc) {
        repository.save(disc);
    }

    public VinylDisc getVinylDisc(Long id) {
        return repository.get(id);
    }

    public void updateVinylDisc(Long id, VinylDisc disc) {
        repository.update(id, disc);
    }

    public void deleteVinylDisc(Long id) {
        repository.delete(id);
    }

    public List<VinylDisc> getAllVinylDiscs() {
        return repository.getAll();
    }

    @Override
    public VinylDiscService create(Repository<Long, VinylDisc> repository) {
        return new VinylDiscServiceImpl(repository);
    }
}
