package org.datapool.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, String> {
    List<Project> findByOwnerId(long ownerId);
    Optional<Project> findByOwnerIdAndName(long ownerId, String name);
    Optional<Project> findByOwnerIdAndId(long ownerId, String projectId);
}
