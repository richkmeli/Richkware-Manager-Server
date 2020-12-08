package it.richkmeli.rms.web.v2;

import it.richkmeli.rms.data.entity.user.model.User;
import it.richkmeli.rms.data.entity.user.UserRepository;
import it.richkmeli.rms.web.v2.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// TODO use elements paging of spring

//@RestController TODO API v2
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    UserController(UserRepository repository) {
        this.userRepository = repository;
    }

    //@RequestMapping(path = "/users2", method = RequestMethod.GET)
    @GetMapping(path = "/users2")
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/admins")
    public Iterable<User> getAllAdmin() {
        return userRepository.findUsersByAdminIsTrue();
    }

    @GetMapping(path = "/user2a/{email}")
    public Optional<User> getOneA(@PathVariable String email) {
        return userRepository.findById(email);
    }

    @GetMapping(path = "/user2b/{email}")
    public User getOneB(@PathVariable String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return user;
    }

    @GetMapping(path = "/user2c/{email}")
    public ResponseEntity<Optional<User>> getOneC(@PathVariable String email) {
        if (!userRepository.existsById(email)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepository.findById(email));
    }

    @PostMapping(path = "/user2", consumes = "application/json")
    public User createUser(@RequestBody User user) {
        user = userRepository.save(user);
        return user;
    }

    @PutMapping("/user2/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable String email) {

        return userRepository.findById(email)
                .map(user -> {
                    user.setPassword(newUser.getPassword());
                    user.setAdmin(newUser.getAdmin());
                    // ignoring Devices and RMCs
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    // change email
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/user2/{id}")
    void deleteUser(@PathVariable String email) {
        userRepository.deleteById(email);
    }


//    @RequestMapping(path = "/user3/{email}", method = RequestMethod.GET)
//    EntityModel<User> getOneWithRefs(@PathVariable String email) {
//
//        User user = userRepository.findById(email)
//                .orElseThrow(() -> new UserNotFoundException(email));
//
//        return new EntityModel<>(user,
//                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getOneB(email)).withSelfRel());
//                //WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAll()).withRel("users2"));
//    }

}
