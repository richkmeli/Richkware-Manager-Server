package it.richkmeli.rms.web.v2;

import it.richkmeli.rms.data.User.User;
import it.richkmeli.rms.data.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    UserController(UserRepository repository) {
        this.userRepository = repository;
    }

    @RequestMapping(path = "/user2", method = RequestMethod.GET)
    public Iterable<User> getUser(@RequestParam String email) {
        return userRepository.findAll();
    }

    @RequestMapping(path = "/user2/{email}", method = RequestMethod.GET)
    public /*ResponseEntity<User>*/Optional<User> getUserById(@PathVariable String email) {
//        if (!userRepository.existsById(email)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        return ResponseEntity.ok(userRepository.getOne(email));
        return userRepository.findById(email);
    }
/*
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Optional<User> getUser(@RequestParam Long id) {
        return userRepository.findById(id);
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST, consumes = "application/json")
    public User createUser(@RequestBody User user) {
        user = userRepository.save(user);
        return user;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
*/


}
