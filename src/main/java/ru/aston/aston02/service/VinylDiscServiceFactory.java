package ru.aston.aston02.service;

import ru.aston.aston02.repository.VinylDiscRepository;

public interface VinylDiscServiceFactory<K, T> {
    VinylDiscService getVinylDiscService(VinylDiscRepository<K, T> repository);
}
