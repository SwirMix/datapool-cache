package org.datapool.core.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnector {
    private Properties config;

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public JdbcConnector(Properties config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("login"),
                config.getProperty("password")
        );
        return con;
    }
}
