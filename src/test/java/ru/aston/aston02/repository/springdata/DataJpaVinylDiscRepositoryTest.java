package ru.aston.aston02.repository.springdata;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.aston02.TestConfig;
import ru.aston.aston02.model.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringJUnitConfig(TestConfig.class)
@Testcontainers
@Import(TestConfig.class)
public class DataJpaVinylDiscRepositoryTest {

    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private CrudVinylDiscRepository crudVinylDiscRepository;

    @Autowired
    private DataJpaVinylDiscRepository repository;

    @Container
    private static GenericContainer<?> postgres = new GenericContainer<>("postgres:13")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "testdb")
            .withEnv("POSTGRES_USER", "test")
            .withEnv("POSTGRES_PASSWORD", "test");


    @Test
    public void save() {
        VinylDisc expected = new VinylDisc("Cold Sweat",
                List.of(new Artist("James", "Brown", Instrument.VOX)),
                List.of(new Song("Cold sweat", 185), new Song("Let yourself go", 135)),
                Genre.FUNK, "King", LocalDate.of(1967, 1, 1));



        repository.save(expected);

        final VinylDisc actual = repository.get(expected.getDiscId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}