package ru.aston.aston02;

import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.VinylDiscRepository;
import ru.aston.aston02.repository.jdbc.JDBCVinylDiscRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private static final String PROPERTIES = "db/postgres.properties";
    private final String url;
    private final String username;
    private final String password;

    private final VinylDiscRepository<Long, VinylDisc> repository;

    private Config() {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
            Properties properties = new Properties();
            if (inputStream == null) {
                throw new FileNotFoundException("File was not found under this path: " + PROPERTIES);
            }

            properties.load(inputStream);

            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            repository = new JDBCVinylDiscRepository(url, username, password);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading properties", e);
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static VinylDiscRepository<Long, VinylDisc> getRepository() {
        return INSTANCE.repository;
    }
}
