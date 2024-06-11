package ru.aston.aston02.service;

import ru.aston.aston02.Config;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.Repository;

public class VinylDiscServiceImplFactory implements VinylDiscServiceFactory<Long, VinylDisc> {

    @Override
    public VinylDiscService getVinylDiscService(Repository<Long, VinylDisc> repository) {
        // OR return new VinylDiscServiceImpl(Config.getRepository());
        // -> then change the signature as well, delete the parameter "repository";
        // Same as in VinylDiscServiceImpl with factory method, don't know which should be erased (:

//        return new VinylDiscServiceImpl(repository);
        return new VinylDiscServiceImpl(Config.getRepository());
    }
}
