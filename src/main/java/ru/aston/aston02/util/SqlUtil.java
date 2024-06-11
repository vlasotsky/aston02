package ru.aston.aston02.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;

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
                throw new SQLTransactionRollbackException("Transaction denied", e);
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
            throw new RuntimeException("Error while executing a single statement", e);
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
