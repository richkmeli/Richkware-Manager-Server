package it.richkmeli.rms.web.v2;

import it.richkmeli.rms.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    AccountController(UserRepository repository) {
        this.userRepository = repository;
    }

  /*  @GetMapping(path = "/LogIn")
    public Response login(@RequestParam )

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
