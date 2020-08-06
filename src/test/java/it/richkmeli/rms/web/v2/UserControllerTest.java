package it.richkmeli.rms.web.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.rms.Application;
import it.richkmeli.rms.data.entity.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Transactional
 //   @Test // TODO API v2
    void getUserById() throws ModelException {
        userController.createUser(new User("test@test.it", "00000000", false));

        Iterable<User> users = userController.getAll();

        ObjectMapper mapper = new ObjectMapper();
        try {
            for (User u : users) {
                String jsonString = mapper.writeValueAsString(u);
                System.out.println(jsonString);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}