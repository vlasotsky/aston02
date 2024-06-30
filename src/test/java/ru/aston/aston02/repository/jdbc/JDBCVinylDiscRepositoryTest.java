package ru.aston.aston02.repository.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.aston.aston02.model.*;
import ru.aston.aston02.repository.VinylDiscRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.aston02.TestData.*;

class JDBCVinylDiscRepositoryTest {
    private static PostgreSQLContainer<?> container;
    private static final Properties PROPERTIES;
    private static VinylDiscRepository<Long, VinylDisc> repository;

    public static String dbUrl;
    public static String dbUsername;
    public static String dbPassword;
    public static String initScript;
    public static final String DB_NAME;

    static {
        DB_NAME = "vinyl_collection";
        PROPERTIES = new Properties();
    }

    @BeforeAll
    static void beforeClass() throws Exception {
        try (InputStream inputStream = JDBCVinylDiscRepositoryTest.class.getClassLoader().getResourceAsStream("db/postgres.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties", e);
        }

        dbUsername = PROPERTIES.getProperty("db.username");
        dbPassword = PROPERTIES.getProperty("db.password");
        initScript = PROPERTIES.getProperty("db.initScript");


        container = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName(DB_NAME)
                .withUsername(dbUsername)
                .withInitScript(initScript)
                .withPassword(dbPassword);
        container.start();

        dbUrl = container.getJdbcUrl();

        repository = new JDBCVinylDiscRepository(dbUrl, dbUsername, dbPassword);
    }

    @AfterAll
    static void afterClass() throws Exception {
        container.stop();
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException, SQLException {
        try (Connection connection = container.createConnection("");
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE vinyl_disc_artist, song, artist, vinyl_disc, genre, instrument RESTART IDENTITY CASCADE;");
        }
        repository.save(DUMMY_DISC);
    }

    @Test
    void save() {
        VinylDisc expected = new VinylDisc("Cold Sweat",
                List.of(new Artist("James", "Brown",
                        Instrument.VOX)),
                List.of(new Song("Cold sweat", 185), new Song("Let yourself go", 135)),
                Genre.FUNK, "King", LocalDate.of(1967, 1, 1));


        repository.save(expected);

        final VinylDisc actual = repository.get(2L);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void get() {
        final VinylDisc actual = repository.get(1L);

        assertEquals(DUMMY_DISC, actual);
    }

    @Test
    void getNotExisting() {
        assertThrows(IllegalArgumentException.class, () -> repository.get(DUMMY_LONG));
    }

    @Test
    void update() {
        final VinylDisc beforeUpdate = repository.get(1L);
        assertEquals(DUMMY_DISC, beforeUpdate);

        repository.update(1L, DUMMY_DISC_CHANGED);

        final VinylDisc actual = repository.get(1L);
        assertEquals(DUMMY_DISC_CHANGED, actual);
    }

    @Test
    void delete() {
        repository.save(DUMMY_DISC_CHANGED);
        final VinylDisc savedDisc = repository.get(2L);

        repository.delete(2L);
        assertThrows(IllegalArgumentException.class, () -> repository.get(2L));
    }

    @Test
    void deleteNotExisting() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete(DUMMY_LONG));
    }

    @Test
    void getAll() {
        final List<VinylDisc> actual = repository.getAll();
        final List<VinylDisc> expected = List.of(DUMMY_DISC);
        assertIterableEquals(expected, actual);
    }
}