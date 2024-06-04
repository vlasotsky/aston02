package ru.aston.aston02.util;

import ru.aston.aston02.model.Instrument;
import ru.aston.aston02.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtil {

    private final ConnectionFactory connectionFactory;

    public SqlUtil(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void executeTransaction(TransactionExecutor executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                executor.execute(connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeStatement(String statement, StatementExecutor executor) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            executor.execute(ps);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int retrieveId(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("Failed to retrieve the ID " + statement.getMetaData());
        }
    }

    public static int getInstrumentId(Connection connection, Instrument instrument) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
                "SELECT instr_id " +
                "FROM instrument " +
                "WHERE instr_name=?")) {

            ps.setString(1, instrument.name());
            return ps.executeQuery().getInt("id");
        }
    }

    public static int getSongId(Connection connection, Song song) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
                "SELECT song_id " +
                "FROM song " +
                "WHERE title=?")) {

            ps.setString(1, song.getTitle());
            return ps.executeQuery().getInt("id");
        }
    }

    @FunctionalInterface
    public interface StatementExecutor {
        void execute(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface TransactionExecutor {
        void execute(Connection connection) throws SQLException;
    }

    public interface ConnectionFactory {
        Connection getConnection() throws SQLException;
    }
}
