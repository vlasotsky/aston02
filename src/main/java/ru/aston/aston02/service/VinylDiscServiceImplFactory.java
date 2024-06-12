package ru.aston.aston02.service;

import ru.aston.aston02.Config;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.Repository;

public class VinylDiscServiceImplFactory implements VinylDiscServiceFactory<Long, VinylDisc> {

    @Override
    public VinylDiscService getVinylDiscService(Repository<Long, VinylDisc> repository) {
        return new VinylDiscServiceImpl(Config.getRepository());
    }
}
