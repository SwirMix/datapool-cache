package org.datapool.services;

import org.datapool.core.jdbc.TableInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AbstractImportService {
    public Set<Map> getBatch(int batchSize, String tableName  );
    public long getSize(String tableName);
    public Map dataSourceInfo();
    public List<Map<String, Object>> showTables();
    public long getTableSize(String tableName);
    public TableInfo getTableInfo(String tableName);
    public ResultSet executeQuery(String query) throws SQLException;

    public Connection getJdbcConnection() throws SQLException;

    public void execute(String query) throws SQLException;

    public Set<String> getResultsColumns(String tableName);

    public void close();
}
