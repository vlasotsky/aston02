package ru.aston.aston02;

import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.repository.Repository;
import ru.aston.aston02.repository.jdbc.JDBCVinylDiscRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private static final String PROPERTIES = "/resources/postgres.properties";
    private String url;
    private String username;
    private String password;

    private Repository<Long, VinylDisc> repository;

    private Config() {
        try (InputStream inputStream = Config.class.getResourceAsStream(PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            repository = new JDBCVinylDiscRepository(url, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static Repository<Long, VinylDisc> getRepository() {
        return INSTANCE.repository;
    }
}
