package org.datapool.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsPagingRepository extends PagingAndSortingRepository<Project, String> {
    public Page<Project> findByName(Pageable pageable, String name);
}
