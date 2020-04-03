package it.richkmeli.rms.web;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class commandTest extends ServletTestManager {
    //http://localhost:8080/Richkware-Manager-Server/command

    @Test
    public void doGet() {

        String jsonInput = "{" +
                "    \"channel\": \"richkware\"," +
                "    \"data0\": \"BbVMwNMgUtglVgJx\"" +
                "}";
        doBefore(jsonInput);

        new command().doGet(requestMock, responseMock);

        //verify(requestMock, atLeast(1)).getParameter("username"); // only if you want to verify username was called...

        String response = doAfter();
        //Assert.assertTrue(response.contains("OK"));
    }

    @Test
    public void doPut() {
    }

    @Test
    public void doPost() {
        String jsonInput = "{" +
                "    \"channel\": \"richkware\"," +
                "    \"device\": \"DEVICE_2\"," +
                "    \"data\": \"command output\"" +
                "}";
        doBefore(jsonInput);

        new command().doPost(requestMock, responseMock);

        //verify(requestMock, atLeast(1)).getParameter("username"); // only if you want to verify username was called...

        String response = doAfter();
        Assert.assertTrue(response.contains("OK"));
    }
}