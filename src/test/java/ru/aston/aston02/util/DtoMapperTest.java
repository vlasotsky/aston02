package ru.aston.aston02.util;

import org.junit.jupiter.api.Test;
import ru.aston.aston02.model.Genre;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.dto.VinylDiscDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.aston.aston02.TestData.*;

class DtoMapperTest {

    @Test
    void getDto() {
        List<VinylDisc> discList = Collections.singletonList(DUMMY_DISC);

        VinylDiscDto dto = DtoMapperUtil.getDto(DUMMY_DISC, discList);

        assertEquals(DUMMY_STRING, dto.getTitle());
        assertEquals(DUMMY_ARTISTS, dto.getArtists());
        assertEquals(DUMMY_SONGS, dto.getSongs());
        assertEquals(Genre.FUNK, dto.getGenre());
        assertEquals(DUMMY_STRING, dto.getLabel());
        assertEquals(DUMMY_LOCAL_DATE, dto.getReleaseDate());
        assertTrue(dto.isFew());
    }

    @Test
    void getTos() {
        List<VinylDisc> discList = Arrays.asList(DUMMY_DISC, DUMMY_DISC_CHANGED);

        List<VinylDiscDto> dtos = DtoMapperUtil.getAllDto(discList);

        assertEquals(2, dtos.size());
        assertEquals(dtos.get(0).getGenre(), dtos.get(0).getGenre());
        assertEquals(dtos.get(1).getGenre(), dtos.get(1).getGenre());
    }
}