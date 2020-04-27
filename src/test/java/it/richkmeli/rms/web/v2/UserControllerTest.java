package it.richkmeli.rms.web.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.richkmeli.rms.Application;
import it.richkmeli.rms.data.model.user.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Transactional
    @Test
    void getUserById(){
        Iterable<User> users = userController.getAll();

        ObjectMapper mapper = new ObjectMapper();
        try {
            for (User u : users) {

                Hibernate.initialize(u.getDevices());
                Hibernate.initialize(u.getRmcs());

                String jsonString = mapper.writeValueAsString(u);
                System.out.println(jsonString);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}