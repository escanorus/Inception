package ru.aracle.inception.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.aracle.inception.Inception;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connection extends DataManager {

    private HikariDataSource dataSource;

    public Connection() {
        if (!Inception.settings.statement("Connection.use")) return;
        String URL = "jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=%s&useSSL=%s";
        String PoolName = "inception-api-%s-%s-%s";
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(URL, address(), port(), database(), publicKeyRetrieval(), ssl()));
        config.setUsername(username());
        config.setPassword(password());
        config.setMinimumIdle(minimumIdle());
        config.setMaximumPoolSize(maximumPoolSize());
        config.setConnectionTimeout(connectionTimeout());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.setPoolName(String.format(PoolName, id(), server(), mode()));
        try {
            dataSource = new HikariDataSource(config);
        } catch (Exception exception) {
            Inception.error(exception.getMessage());
        }
        if (dataSource == null) Inception.instance.getServer().getPluginManager().disablePlugin(Inception.instance);
    }

    @Override
    public void close() {
        if (!Inception.settings.statement("Connection.use")) return;
        try {
            if (dataSource != null && connection() != null) connection.close();
        } catch (SQLException throwables) {
            Inception.error(throwables.getMessage());
        }
    }

    @Override
    public ResultSet get(String query) {
        {
            ResultSet result = null;
            try {
                if (connection() != null) {
                    connection = connection();
                    result = connection.prepareStatement(query).executeQuery();
                }
            } catch (SQLException throwables) {
                Inception.error(throwables.getMessage());
            }

            return result;
        }
    }

    @Override
    public int set(String query) {
        int result = 0;
        try {
            if (connection() != null) connection = connection();

            PreparedStatement statement = connection.prepareStatement(query);
            result = statement.executeUpdate();

            statement.close();
        } catch (SQLException throwables) {
            Inception.error(throwables.getMessage());
        }
        return result;
    }

    @Override
    public java.sql.Connection connection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException throwables) {
            Inception.error(throwables.getMessage());
        }
        return connection;
    }

    public static String address() {
        return Inception.settings.string("Connection.address");
    }

    public static String port() {
        return String.valueOf(Inception.settings.integer("Connection.port"));
    }

    public static String database() {
        return Inception.settings.string("Connection.database");
    }

    public static String username() {
        return Inception.settings.string("Connection.username");
    }

    public static String password() {
        return Inception.settings.string("Connection.password");
    }

    public static String publicKeyRetrieval() {
        return String.valueOf(Inception.settings.statement("Connection.publicKeyRetrieval"));
    }

    public static String ssl() {
        return String.valueOf(Inception.settings.statement("Connection.ssl"));
    }

    public static Integer minimumIdle() {
        return Inception.settings.integer("Connection.minimumIdle");
    }

    public static Integer maximumPoolSize() {
        return Inception.settings.integer("Connection.maximumPoolSize");
    }

    public static Integer connectionTimeout() {
        return Inception.settings.integer("Connection.connectionTimeout");
    }

    public static String id() {
        return Inception.settings.integer("Connection.id").toString();
    }

    public static String server() {
        return Inception.settings.string("Connection.Server");
    }

    public static String mode() {
        return Inception.settings.string("Connection.Mode");
    }
}
