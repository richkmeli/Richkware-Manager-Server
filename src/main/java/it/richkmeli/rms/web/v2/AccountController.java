package it.richkmeli.rms.web.v2;

import it.richkmeli.jframework.auth.web.account.LogInJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.model.rmc.Rmc;
import it.richkmeli.rms.data.model.user.UserRepository;
import it.richkmeli.rms.web.v1.util.RMSServletManager;
import it.richkmeli.rms.web.v1.util.RMSSession;
import org.springframework.web.bind.annotation.*;
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
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException, DatabaseException {
            //RMSServletManager rmsServletManager = new RMSServletManager(request,response);
            RMSServletManager rmsServletManager = new RMSServletManager(authServletManager);
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            if (rmsSession != null) {
                if (rmsSession.getChannel() != null) {
                    if (rmsSession.getChannel().equalsIgnoreCase(RMSServletManager.Channel.RMC)) {
                        Rmc rmc = new Rmc(rmsSession.getUserID(), rmsSession.getRmcID());
                        Logger.info("RMC: " + rmc.getAssociatedUser() + " - " + rmc.getRmcId());
                        if (!rmsSession.getRmcDatabaseManager().checkRmcUserPair(rmc)) {
                            if (rmsSession.getRmcDatabaseManager().checkRmcUserPair(new Rmc("", rmsSession.getRmcID()))) {
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


    AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/LogIn2")
    public void login() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest, httpServletResponse);
        logIn.doAction(rmsServletManager);
    }

//    @GetMapping(path = "/LogIn")
//    public Response login(HttpSession session, @RequestParam) {
//        session.setAttribute(Constants.FOO, new Foo());
//        //...
//        Foo foo = (Foo) session.getAttribute(Constants.FOO);
//    }


}
