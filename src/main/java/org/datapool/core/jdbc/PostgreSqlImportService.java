package org.datapool.core.jdbc;

import org.datapool.services.AbstractImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class PostgreSqlImportService implements AbstractImportService {
    private JdbcConnector connector;
    private static final String SHOW_TABLES_TEMPLATE = "select distinct(table_name) from information_schema.columns where table_schema='%s'";
    private static final String CALC_TABLE_WEIGHT = "SELECT pg_total_relation_size('%s')";
    private static final String CALC_TABLE_SIZE = "SELECT count(*) as size from %s";
    private static final String COLUMNS_INFO_BY_TABLE = "select column_name from information_schema.columns where table_schema='%s' and table_name = '%s'";
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public PostgreSqlImportService(Properties config){
        this.connector = new JdbcConnector(config);
    }

    @Override
    public Set<Map> getBatch(int batchSize, String tableName) {
        return null;
    }

    @Override
    public long getSize(String tableName) {
        String query = String.format(CALC_TABLE_SIZE, tableName);
        List<Map<String, Object>> tables = getQueryResult(query);
        return (long) tables.get(0).get("size");
    }

    @Override
    public Map dataSourceInfo() {
        return null;
    }

    @Override
    public List<Map<String, Object>> showTables() {
        String query = String.format(SHOW_TABLES_TEMPLATE, connector.getConfig().get("Schema"));
        List<Map<String, Object>> tables = getQueryResult(query);
        return tables;
    }

    @Override
    public Set<String> getResultsColumns(String tableName){
        String query = String.format(COLUMNS_INFO_BY_TABLE, connector.getConfig().get("Schema"), tableName);
        List<Map<String, Object>> results = getQueryResult(query);
        Set<String> columns = new TreeSet<>();
        for (Map<String, Object> line : results){
            columns.add((String) line.get("column_name"));
        }
        return columns;
    }

    @Override
    public void close() {

    }


    private List<Map<String, Object>> getQueryResult(String query){
        List<Map<String, Object>> data = new ArrayList();
        try {
            Statement statement = connector.getConnection().createStatement();
            statement.setFetchSize(100);
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            logger.debug("QUERY: " + query + "\n" + "COLUMNS: " + columns);
            while(resultSet.next())
            {
                data.add(rowProcessing(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static Map<String, Object> rowProcessing(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Map<String, Object> row = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        String[] columns = new String[metaData.getColumnCount()];
        for (int index = 0; index < metaData.getColumnCount(); index++){
            columns[index] = metaData.getColumnName(index+1);
            Object value = resultSet.getObject(index+1);
            row.put(metaData.getColumnName(index+1), value);
        }

        return row;
    }

    @Override
    public long getTableSize(String tableName) {
        String query = String.format(CALC_TABLE_WEIGHT, tableName);
        List<Map<String, Object>> tables = getQueryResult(query);
        return (long) tables.get(0).get("pg_total_relation_size");
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        return new TableInfo(getSize(tableName), tableName, getTableSize(tableName), getResultsColumns(tableName));
    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connector.getConnection().createStatement();
        statement.setFetchSize(300000);
        return statement.executeQuery(query);
    }

    @Override
    public Connection getJdbcConnection() throws SQLException {
        return connector.getConnection();
    }


    @Override
    public void execute(String query) throws SQLException {
        Connection connection = connector.getConnection();
        Statement statement = connection.createStatement();
        statement.setFetchSize(300000);
        statement.execute(query);
        connection.close();
    }
}
