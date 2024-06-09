package ru.aston.aston02.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.Repository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.aston02.TestData.DUMMY_DISC;

@ExtendWith(MockitoExtension.class)
class VinylDiscServiceImplTest {

    @Mock
    private Repository<Long, VinylDisc> repository;

    @InjectMocks
    private VinylDiscServiceImpl service;

    @Test
    void save() {
        doNothing().when(repository).save(any(VinylDisc.class));
        service.saveVinylDisc(DUMMY_DISC);

        verify(repository, times(1)).save(DUMMY_DISC);
    }

    @Test
    void get() {
        when(service.getVinylDisc(1L)).thenReturn(DUMMY_DISC);
        final VinylDisc disc = service.getVinylDisc(1L);

        assertEquals(DUMMY_DISC, disc);
        verify(repository, times(1)).get(1L);
    }

    @Test
    void update() {
        doNothing().when(repository).update(eq(1L), any(VinylDisc.class));

        service.updateVinylDisc(1L, DUMMY_DISC);
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
        List<VinylDisc> expected = Collections.singletonList(DUMMY_DISC);
        when(repository.getAll()).thenReturn(expected);

        List<VinylDisc> actual = service.getAllVinylDiscs();
        assertEquals(expected, actual);
        verify(repository, times(1)).getAll();
    }
}