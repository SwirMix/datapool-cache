package org.datapool.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);
    public Users findByLogin(String login);
}
