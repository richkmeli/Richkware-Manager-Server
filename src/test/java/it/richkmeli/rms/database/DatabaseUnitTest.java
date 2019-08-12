package it.richkmeli.rms.database;

import it.richkmeli.jframework.auth.AuthDatabaseManager;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.data.rmc.RMCDatabaseManager;
import it.richkmeli.rms.data.rmc.model.RMC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUnitTest {

  /*  private AuthDatabaseManager adm;
    private DeviceDatabaseManager ddm;
    private RMCDatabaseManager rdm;

    @Before
    public void setUp() throws Exception {
        //Creazione del DB di test
        try {
            adm = new AuthDatabaseManager();
            ddm = new DeviceDatabaseManager();
            rdm = new RMCDatabaseManager();
            adm.addUser(new User("richk@i.it", "00000000", true));
            adm.addUser(new User("er@fv.it", "00000000", false));
            adm.addUser(new User("test@i.it", "00000000", true));

            ddm.addDevice(new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "start##start##start", ""));
            ddm.addDevice(new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", ""));
            ddm.addDevice(new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it", "", ""));

            rdm.addRMC(new RMC("richk@i.it", "test_rmc_ID"));
            rdm.addRMC(new RMC("er@fv.it", "test_rmc_ID_2"));
            rdm.addRMC(new RMC("er@fv.it", "test_rmc_ID_3"));
            assert true;
        } catch (DatabaseException e) {
            e.printStackTrace();
            Logger.error("Session TEST USERS", e);
            assert false;
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void refreshDevice() {
        //Test per l'aggiornamento della lista dei dispositivi per account admin
        try {
            List<Device> lista = ddm.refreshDevice();

            Device d2 = new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "start##start##start", "");
            Device d3 = new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", "");
            Device d1 = new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it", "", "");

            List<Device> expected = new ArrayList<>();
            expected.add(d1);
            expected.add(d2);
            expected.add(d3);

            assert lista.toString().equals(expected.toString());

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
*/
    /*@Test
    public void testRefreshDevice() {
    }

    @Test
    public void addDevice() {
    }

    @Test
    public void editDevice() {
    }

    @Test
    public void getDevice() {
    }

    @Test
    public void removeDevice() {
    }

    @Test
    public void getEncryptionKey() {
    }

    @Test
    public void editCommands() {
    }

    @Test
    public void getCommands() {
    }

    @Test
    public void setCommandsOutput() {
    }

    @Test
    public void getCommandsOutput() {
    }

    @Test
    public void checkPassword() {
    }*/
}