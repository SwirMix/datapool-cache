package org.datapool.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "settings")
public class Setting {
    @Id
    private String name;
    @Column
    private String value;

    public String getName() {
        return name;
    }

    public Setting setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Setting setValue(String value) {
        this.value = value;
        return this;
    }
}
