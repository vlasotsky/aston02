package ru.aston.aston02.service;

import ru.aston.aston02.model.dto.VinylDiscDto;

import java.util.List;

public interface VinylDiscService {
    void saveVinylDisc(VinylDiscDto disc);

    VinylDiscDto getVinylDisc(Long id);

    void updateVinylDisc(Long id, VinylDiscDto disc);

    void deleteVinylDisc(Long id);

    List<VinylDiscDto> getAllVinylDiscs();
}
