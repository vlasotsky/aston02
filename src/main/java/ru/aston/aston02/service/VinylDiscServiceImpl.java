package ru.aston.aston02.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.dto.VinylDiscDto;
import ru.aston.aston02.model.dto.VinylDiscDtoMapper;
import ru.aston.aston02.repository.VinylDiscRepository;

import java.util.List;

@Service
public class VinylDiscServiceImpl implements VinylDiscService, VinylDiscServiceFactory<Long, VinylDisc> {

    private final VinylDiscRepository<Long, VinylDisc> repository;
    private final VinylDiscDtoMapper mapper = Mappers.getMapper(VinylDiscDtoMapper.class);

    public VinylDiscServiceImpl(VinylDiscRepository<Long, VinylDisc> repository) {
        this.repository = repository;
    }

    public void saveVinylDisc(VinylDiscDto disc) {
        repository.save(mapper.toEntity(disc));
    }

    public VinylDiscDto getVinylDisc(Long id) {
        return mapper.toDto(repository.get(id), repository.getAll());
    }

    public void updateVinylDisc(Long id, VinylDiscDto disc) {
        repository.update(id, mapper.toEntity(disc));
    }

    public void deleteVinylDisc(Long id) {
        repository.delete(id);
    }

    public List<VinylDiscDto> getAllVinylDiscs() {
        return mapper.toDtoAll(repository.getAll());
    }

    @Override
    public VinylDiscService getVinylDiscService(VinylDiscRepository<Long, VinylDisc> repository) {
        return new VinylDiscServiceImpl(repository);
    }
}
