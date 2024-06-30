package ru.aston.aston02.repository.springdata;

import org.springframework.data.repository.CrudRepository;
import ru.aston.aston02.model.VinylDisc;

public interface CrudVinylDiscRepository extends CrudRepository<VinylDisc, Long> {
}
