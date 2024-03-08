package com.rinha.spring;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
    
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    static {
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/rinha");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setValidationTimeout(5000);
        config.setConnectionTimeout(30000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    private HikariCPDataSource(){}

}