package org.datapool.core.jdbc;

import java.util.Set;

public class TableInfo {
    private long rows;
    private String name;
    private long tableWeight;
    private Set<String> columns;

    public Set<String> getColumns() {
        return columns;
    }

    public TableInfo setColumns(Set<String> columns) {
        this.columns = columns;
        return this;
    }

    public TableInfo(){

    }

    public TableInfo(long rows, String name, long tableWeight, Set<String> columns){
        this.name = name;
        this.rows = rows;
        this.tableWeight = tableWeight;
        this.columns = columns;
    }

    public long getRows() {
        return rows;
    }

    public TableInfo setRows(long rows) {
        this.rows = rows;
        return this;
    }

    public String getName() {
        return name;
    }

    public TableInfo setName(String name) {
        this.name = name;
        return this;
    }

    public long getTableWeight() {
        return tableWeight;
    }

    public TableInfo setTableWeight(long tableWeight) {
        this.tableWeight = tableWeight;
        return this;
    }
}
