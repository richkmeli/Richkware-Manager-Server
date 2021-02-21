package it.richkmeli.rms.web.v2.entity;

import it.richkmeli.jframework.auth.web.account.UserJob;
import it.richkmeli.jframework.auth.web.account.UsersJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.rms.data.entity.user.UserRepository;
import it.richkmeli.rms.web.v1.util.RMSServletManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    private final UserRepository userRepository;
    UserJob userJob = new UserJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException {

        }
    };

    UsersJob usersListJob = new UsersJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException {

        }
    };

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(name = "user", path = "/user")
    public void getUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        userJob.doGet(rmsServletManager);
    }

    @DeleteMapping(name = "user", path = "/user")
    public void deleteUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        userJob.doDelete(rmsServletManager);
    }


    @GetMapping(name = "users", path = "/users")
    public void getUsers() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        usersListJob.doGet(rmsServletManager);
    }


}
