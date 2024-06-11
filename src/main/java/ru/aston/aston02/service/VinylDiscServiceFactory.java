package ru.aston.aston02.service;

import ru.aston.aston02.repository.Repository;

interface VinylDiscServiceFactory<K, T> {
    VinylDiscService getVinylDiscService(Repository<K, T> repository);
}
