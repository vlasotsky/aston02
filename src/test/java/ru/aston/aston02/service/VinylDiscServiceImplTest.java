package ru.aston.aston02.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.dto.VinylDiscDto;
import ru.aston.aston02.model.dto.VinylDiscDtoMapper;
import ru.aston.aston02.repository.VinylDiscRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.aston02.TestData.DUMMY_DISC;
import static ru.aston.aston02.TestData.DUMMY_DISC_DTO;

@ExtendWith(MockitoExtension.class)
class VinylDiscServiceImplTest {

    @Mock
    private VinylDiscRepository<Long, VinylDisc> repository;

    @Mock
    private VinylDiscDtoMapper mapper;

    @InjectMocks
    private VinylDiscServiceImpl service;

    @Test
    void save() {
        doNothing().when(repository).save(any(VinylDisc.class));
        service.saveVinylDisc(DUMMY_DISC_DTO);

        verify(repository, times(1)).save(DUMMY_DISC);
    }

    @Test
    void get() {
        when(service.getVinylDisc(1L)).thenReturn(DUMMY_DISC_DTO);
//        when(repository.get(1L)).thenReturn(DUMMY_DISC);
        when(mapper.toDto(DUMMY_DISC, anyList())).thenReturn(DUMMY_DISC_DTO);

        final VinylDiscDto disc = service.getVinylDisc(1L);

        assertEquals(DUMMY_DISC_DTO, disc);
        verify(repository, times(1)).get(1L);
    }

    @Test
    void update() {
        doNothing().when(repository).update(eq(1L), any(VinylDisc.class));

        service.updateVinylDisc(1L, DUMMY_DISC_DTO);
        verify(repository, times(1)).update(1L, DUMMY_DISC);
    }

    @Test
    void delete() {
        doNothing().when(repository).delete(1L);

        service.deleteVinylDisc(1L);
        verify(repository, times(1)).delete(1L);
    }

    @Test
    void getAll() {
        List<VinylDiscDto> expected = Collections.singletonList(DUMMY_DISC_DTO);
        when(service.getAllVinylDiscs()).thenReturn(expected);
        when(repository.getAll()).thenReturn(Collections.singletonList(DUMMY_DISC));

        List<VinylDiscDto> actual = service.getAllVinylDiscs();

        assertEquals(expected, actual);
        verify(repository, times(1)).getAll();
    }
}