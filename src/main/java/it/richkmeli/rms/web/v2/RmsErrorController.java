package it.richkmeli.rms.web.v2;

import it.richkmeli.jframework.util.log.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

//@Controller
public class RmsErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            Logger.error("Error, HttpStatus: " + statusCode);
            /*if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error.html-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error.html-500";
            }*/
        }
        return "forward:/index.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}