package ru.aston.aston02.repository.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.aston.aston02.repository.VinylDiscRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class JDBCVinylDiscRepositoryTest {
    private static PostgreSQLContainer<?> container;
    private static final Properties PROPERTIES;
    private static VinylDiscRepository repository;

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

    @Test
    void save() {
    }

    @Test
    void get() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }
}