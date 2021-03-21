package it.richkmeli.rms.web.v2.account;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.jframework.auth.web.account.LogInJob;
import it.richkmeli.jframework.auth.web.account.LogOutJob;
import it.richkmeli.jframework.auth.web.account.SignUpJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;
import it.richkmeli.rms.data.entity.user.AuthDatabaseSpringManager;
import it.richkmeli.rms.data.entity.user.UserRepository;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {
    private final UserRepository userRepository;
    //@Autowired
    //private HttpServletRequest httpServletRequest;
    //private HttpServletResponse httpServletResponse;
    LogInJob logIn = new LogInJob() {

        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException, AuthDatabaseException {
            //RMSServletManager rmsServletManager = new RMSServletManager(request,response);
            RMSServletManager rmsServletManager = new RMSServletManager(authServletManager);
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            if (rmsSession != null) {
                if (rmsSession.getChannel() != null) {
                    if (rmsSession.getChannel().equalsIgnoreCase(RMSServletManager.Channel.RMC)) {
                        Rmc rmc = new Rmc(AuthDatabaseSpringManager.getInstance().findUserByEmail(rmsSession.getUserID()), rmsSession.getRmcID());
                        Logger.info("RMC: " + rmc.getAssociatedUser() + " - " + rmc.getRmcId());
                        if (!rmsSession.getRmcDatabaseManager().checkRmcUserPair(rmc)) {
                            if (rmsSession.getRmcDatabaseManager().checkRmcUserPair(new Rmc(null, rmsSession.getRmcID()))) {
                                rmsSession.getRmcDatabaseManager().editRMC(rmc);
                            } else {
                                rmsSession.getRmcDatabaseManager().addRMC(rmc);
                            }
                        }
                    }
                } else {
                    Logger.error("channel rmsSession is null");
                }
            } else {
                Logger.error("rmsSession is null");
            }
        }
    };

    LogOutJob logOut = new LogOutJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) {

        }
    };

    SignUpJob signUp = new SignUpJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) {

        }
    };


    AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(name = "LogIn", path = "/LogIn", method = {RequestMethod.GET, RequestMethod.POST})
    public void logIn() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        logIn.doAction(rmsServletManager);
    }

    @RequestMapping(name = "LogOut", path = "/LogOut", method = {RequestMethod.GET, RequestMethod.POST})
    public void logOut() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        logOut.doAction(rmsServletManager);
    }

    @RequestMapping(name = "SignUp", path = "/SignUp", method = {RequestMethod.GET, RequestMethod.POST})
    public void signUp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        signUp.doAction(rmsServletManager);
    }


}
