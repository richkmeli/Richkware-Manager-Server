package it.richkmeli.rms.data.entity.user;

import it.richkmeli.jframework.auth.data.AuthDatabaseModel;
import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.rms.data.entity.user.model.User;
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

    @Autowired
    public AuthDatabaseSpringManager(UserRepository userRepository) throws AuthDatabaseException {
        this.userRepository = userRepository;
    }

    public static AuthDatabaseSpringManager getInstance() throws AuthDatabaseException {
        return new AuthDatabaseSpringManager(userRepository);
    }

//    public it.richkmeli.jframework.auth.model.User findUserByEmail(String email) throws AuthDatabaseException {
//        User user = userRepository.findUserByEmail(email);
//        try {
//            return new it.richkmeli.jframework.auth.model.User(user.getEmail(), user.getPassword(), user.getAdmin());
//        } catch (ModelException e) {
//            throw new AuthDatabaseException(e);
//        }
//    }

    public User findUserByEmail(String email) throws AuthDatabaseException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<it.richkmeli.jframework.auth.model.User> getAllUsers() throws AuthDatabaseException {
        List<User> users = userRepository.findAll();
        List<it.richkmeli.jframework.auth.model.User> users1 = new ArrayList<>();
        for (User u : users) {
            try {
                users1.add(new it.richkmeli.jframework.auth.model.User(u.getEmail(), u.getPassword(), u.getAdmin()));
            } catch (ModelException e) {
                throw new AuthDatabaseException(e);
            }
        }
        return users1;
    }

    @Override
    public boolean addUser(it.richkmeli.jframework.auth.model.User user) throws AuthDatabaseException {
        try {
            userRepository.save(new User(
                    user.getEmail(),
                    Crypto.hashPassword(user.getPassword(), false),
                    user.getAdmin()));
            return true;
        } catch (ModelException e) {
            throw new AuthDatabaseException(e);
        }
    }

    public User addUser(User user) throws AuthDatabaseException {
        try {
            return userRepository.save(new User(
                    user.getEmail(),
                    Crypto.hashPassword(user.getPassword(), false),
                    user.getAdmin()));
        } catch (ModelException e) {
            throw new AuthDatabaseException(e);
        }
    }

    @Override
    public boolean removeUser(String s) throws AuthDatabaseException, ModelException {
        User user = findUserByEmail(s);
        if(user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserPresent(String s) throws AuthDatabaseException, ModelException {
        return userRepository.existsUserByEmail(s);
    }

    @Override
    public boolean editPassword(String email, String password) throws AuthDatabaseException, ModelException {
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
    public boolean editAdmin(String email, Boolean aBoolean) throws AuthDatabaseException, ModelException {
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
    public boolean checkPassword(String email, String password) throws AuthDatabaseException, ModelException {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            Crypto.verifyPassword(user.getPassword(), password);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAdmin(String email) throws AuthDatabaseException, ModelException {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return user.getAdmin();
        } else {
            return false;
        }
    }
}