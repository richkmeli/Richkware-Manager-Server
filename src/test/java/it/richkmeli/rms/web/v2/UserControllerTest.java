package it.richkmeli.rms.web.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.rms.Application;
import it.richkmeli.rms.data.entity.user.model.User;
import it.richkmeli.rms.web.v2.test.UserControllerExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    //@Autowired
    //private UserControllerExample userController;

    @Autowired
    private MockMvc mockMvc;

    //@Test
    public void shouldReturnDefaultMessage() throws Exception {
        String jsonInput = "{" +
                "    \"channel\": \"richkware\"," +
                "    \"data0\": \"BbVMwNMgUtglVgJx\"" +
                "}";
        this.mockMvc.perform(get("/command").content(jsonInput))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }



//    @Transactional
// //   @Test // TODO_ API v2
//    void getUserById() throws ModelException {
//        userController.createUser(new User("test@test.it", "00000000", false));
//
//        Iterable<User> users = userController.getAll();
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            for (User u : users) {
//                String jsonString = mapper.writeValueAsString(u);
//                System.out.println(jsonString);
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }
}