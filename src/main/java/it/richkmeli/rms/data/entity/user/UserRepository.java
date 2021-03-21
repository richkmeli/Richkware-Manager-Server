package it.richkmeli.rms.data.entity.user;

import it.richkmeli.rms.data.entity.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Iterable<User> findUsersByAdminIsTrue();

    boolean existsUserByEmail(String email);
    void deleteUserByEmail(String email);
    User findUserByEmail(String email);
}
