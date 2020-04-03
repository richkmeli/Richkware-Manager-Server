package it.richkmeli.rms.web;

import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ServletTestManager extends Mockito {
    @Mock
    protected HttpServletRequest requestMock;
    @Mock
    protected HttpServletResponse responseMock;
    @Mock
    protected HttpSession sessionMock;
    @Mock
    protected RMSServletManager rmsServletManagerMock;
    @Mock
    protected RMSSession rmsSessionMock;
    @InjectMocks
    protected DeviceDatabaseManager deviceDatabaseManagerMock;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    protected void doBefore(String jsonInput) {
        try {
            when(requestMock.getSession()).thenReturn(sessionMock);
            when(requestMock.getParameterNames()).thenReturn(Collections.enumeration(new ArrayList<String>()));
            // Body
            when(requestMock.getReader()).thenReturn(new BufferedReader(
                    new InputStreamReader(
                            new ByteArrayInputStream(
                                    (jsonInput).getBytes()
                            ))));

            when(rmsServletManagerMock.getRMSServerSession()).thenReturn(rmsSessionMock);

            when(rmsSessionMock.getDeviceDatabaseManager()).thenReturn(deviceDatabaseManagerMock);
            //test.loadRandomTest(rmsSession);

            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
            when(responseMock.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String doAfter() {
        String response = "";
        writer.flush(); // it may not have been flushed yet...
        response = stringWriter.toString();

        System.out.println(response);
        return response;
    }
}
