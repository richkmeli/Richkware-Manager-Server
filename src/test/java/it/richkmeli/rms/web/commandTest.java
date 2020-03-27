package it.richkmeli.rms.web;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class commandTest extends Mockito {
    //http://localhost:8080/Richkware-Manager-Server/command

    @Test
    public void doGet() throws IOException {
        /*
        {
    "channel": "richkware",
    "data0": "BbVMwNMgUtglVgJx"
}
         */

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

       when(request.getParameter("channel")).thenReturn("richkware");
       when(request.getParameter("data0")).thenReturn("BbVMwNMgUtglVgJx");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new command().doPost(request, response);

        //verify(request, atLeast(1)).getParameter("username"); // only if you want to verify username was called...
        writer.flush(); // it may not have been flushed yet...
        System.out.println(stringWriter);
        //Assert.assertTrue(stringWriter.toString().contains("OK"));
    }

    @Test
    public void doPut() {
    }

    @Test
    public void doPost() {
        /*
        {
    "channel": "richkware",
    "device": "DEVICE_2",
    "data": "command output"
}
         */
    }
}