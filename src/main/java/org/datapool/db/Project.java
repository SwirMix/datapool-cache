package org.datapool.db;

import javax.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private long ownerId;

    public String getId() {
        return id;
    }

    public Project setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public Project setOwnerId(long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
