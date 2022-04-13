package ru.aracle.inception.connection;

import ru.aracle.inception.Inception;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DataManager {
    protected Connection connection;

    protected DataManager() {
        this.connection = null;
    }

    public Connection connection() {
        return connection;
    }

    public void close() {
        if (!Inception.settings.statement("Connection.use")) return;
        try {
            if (connection() != null) connection.close();
        } catch (SQLException throwables) {
            Inception.error(throwables.getMessage());
        }
    }

    public abstract ResultSet get(String query);

    public abstract int set(String query);
}
