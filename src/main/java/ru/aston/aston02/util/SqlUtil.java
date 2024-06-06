package ru.aston.aston02.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
