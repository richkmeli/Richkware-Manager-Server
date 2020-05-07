package it.richkmeli.rms.data.model.user;

import it.richkmeli.jframework.auth.AuthDatabaseModel;
import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.orm.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Using Spring/Hibernate ORM
 */

@Component
public class AuthDatabaseSpringManager implements AuthDatabaseModel {
    private static UserRepository userRepository;

    public static AuthDatabaseSpringManager getInstance() throws DatabaseException {
        return new AuthDatabaseSpringManager(userRepository);
    }

    @Autowired
    public AuthDatabaseSpringManager(UserRepository userRepository) throws DatabaseException {
        this.userRepository = userRepository;
    }

    @Override
    public List<it.richkmeli.jframework.auth.model.User> getAllUsers() throws DatabaseException {
        List<it.richkmeli.rms.data.model.user.User> users = userRepository.findAll();
        List<it.richkmeli.jframework.auth.model.User> users1 = new ArrayList<>();
        for (it.richkmeli.rms.data.model.user.User u : users) {
            try {
                users1.add(new it.richkmeli.jframework.auth.model.User(u.getEmail(),u.getPassword(),u.getAdmin()));
            } catch (ModelException e) {
                throw new DatabaseException(e);
            }
        }
        return users1;
    }

    @Override
    public boolean addUser(it.richkmeli.jframework.auth.model.User user) throws DatabaseException {
        try {
            userRepository.save(new it.richkmeli.rms.data.model.user.User(
                    user.getEmail(),
                    Crypto.hashPassword(user.getPassword(), false),
                    user.getAdmin()));
            return true;
        } catch (ModelException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean removeUser(String s) throws DatabaseException, ModelException {
        userRepository.deleteById(s);
        return true;
    }

    @Override
    public boolean isUserPresent(String s) throws DatabaseException, ModelException {
        userRepository.existsById(s);
        return true;
    }

    @Override
    public boolean editPassword(String email, String password) throws DatabaseException, ModelException {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            User user1 = user.get();
            user1.setPassword(Crypto.hashPassword(password, false));
            userRepository.save(user1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean editAdmin(String email, Boolean aBoolean) throws DatabaseException, ModelException {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            User user1 = user.get();
            user1.setAdmin(aBoolean);
            userRepository.save(user1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkPassword(String email, String password) throws DatabaseException, ModelException {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            Crypto.verifyPassword(user.get().getPassword(), password);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAdmin(String email) throws DatabaseException, ModelException {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            return user.get().getAdmin();
        } else {
            return false;
        }
    }
}