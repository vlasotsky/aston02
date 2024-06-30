package ru.aston.aston02.repository.springdata;

import org.springframework.stereotype.Repository;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.VinylDiscRepository;

import java.util.List;

@Repository
public class DataJpaVinylDiscRepository implements VinylDiscRepository<Long, VinylDisc> {

    private CrudVinylDiscRepository crudVinylDiscRepository;

    public DataJpaVinylDiscRepository() {
    }

    public DataJpaVinylDiscRepository(CrudVinylDiscRepository crudVinylDiscRepository) {
        this.crudVinylDiscRepository = crudVinylDiscRepository;
    }

    @Override
    public void save(VinylDisc disc) {
        crudVinylDiscRepository.save(disc);
    }

    @Override
    public VinylDisc get(Long id) {
        return crudVinylDiscRepository.findById(id).orElse(null);
    }

    @Override
    public void update(Long id, VinylDisc disc) {
        crudVinylDiscRepository.save(disc);
    }

    @Override
    public void delete(Long id) {
        crudVinylDiscRepository.deleteById(id);
    }

    @Override
    public List<VinylDisc> getAll() {
        return (List<VinylDisc>) crudVinylDiscRepository.findAll();
    }
}
