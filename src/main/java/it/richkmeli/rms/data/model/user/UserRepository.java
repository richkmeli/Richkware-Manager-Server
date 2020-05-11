package it.richkmeli.rms.data.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Iterable<User> findUsersByAdminIsTrue();
}
