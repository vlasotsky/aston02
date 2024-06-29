package ru.aston.aston02.util;

import ru.aston.aston02.model.Genre;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.to.VinylDiscDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DtoMapper {

    // Check jenkins (3)

    public static final int SUFFICIENT_AMOUNT_FOR_COLLECTION = 3;

    private DtoMapper() {
    }

    public static VinylDiscDto getDto(VinylDisc disc, List<VinylDisc> discList) {
        final Long numDiscsInGenre = getGenreCount(discList).get(disc.getGenre());
        boolean isFew = numDiscsInGenre < SUFFICIENT_AMOUNT_FOR_COLLECTION;
        return createTo(disc, isFew);
    }

    public static List<VinylDiscDto> getAllDtos(List<VinylDisc> discList) {
        final Map<Genre, Long> genreCount = getGenreCount(discList);
        return discList.stream()
                .map(disc -> createTo(disc, genreCount.get(disc.getGenre()) < SUFFICIENT_AMOUNT_FOR_COLLECTION))
                .collect(Collectors.toList());
    }

    public static VinylDisc getEntity(VinylDiscDto discDto) {
        return new VinylDisc(discDto);
    }

    private static Map<Genre, Long> getGenreCount(List<VinylDisc> discList) {
        return discList.stream()
                .collect(Collectors.groupingBy(VinylDisc::getGenre, Collectors.counting()));
    }

    private static VinylDiscDto createTo(VinylDisc disc, boolean isFew) {
        return new VinylDiscDto(disc.getTitle(), disc.getArtists(), disc.getSongs(), disc.getGenre(), disc.getLabel(), disc.getReleaseDate(), isFew);
    }
}
