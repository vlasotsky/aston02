package ru.aston.aston02.util;

import ru.aston.aston02.model.Genre;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.to.VinylDiscTo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DtoConverter {

    public static List<VinylDiscTo> getTos(List<VinylDisc> discList) {
        final Map<Genre, Long> discsInGenre = discList.stream()
                .collect(Collectors.groupingBy(VinylDisc::getGenre, Collectors.counting()));

        return discList.stream()
                .map(disc -> createTo(disc, discsInGenre.get(disc.getGenre()) < 3))
                .collect(Collectors.toList());
    }

    public static VinylDiscTo createTo(VinylDisc disc, boolean isFew) {
        return new VinylDiscTo(disc.getTitle(), disc.getArtists(), disc.getSongs(), disc.getLabel(), disc.getReleaseYear(), isFew);
    }
}
