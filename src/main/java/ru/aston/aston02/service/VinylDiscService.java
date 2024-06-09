package ru.aston.aston02.service;

import ru.aston.aston02.model.VinylDisc;

import java.util.List;

public interface VinylDiscService {
    void saveVinylDisc(VinylDisc disc);

    VinylDisc getVinylDisc(Long id);

    void updateVinylDisc(Long id, VinylDisc disc);

    void deleteVinylDisc(Long id);

    List<VinylDisc> getAllVinylDiscs();
}
