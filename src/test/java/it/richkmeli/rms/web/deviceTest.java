package it.richkmeli.rms.web;

import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.crypto.exception.CryptoException;
import org.junit.Before;
import org.junit.Test;

import java.util.ResourceBundle;

public class deviceTest {
    private String password;

    @Before
    public void setUp() throws Exception {
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
    }

    @Test
    public void doPut() {
        String data0 = Crypto.encryptRC4("DEVICE_2", password);
        String data1 = Crypto.encryptRC4("9000", password);
        String data2 = Crypto.encryptRC4("richk@i.it", password);

        System.out.println("data0:" + data0 +
                "\ndata1:" + data1 +
                "\ndata2:" + data2);

        try {
            System.out.println("data0dec:" + Crypto.decryptRC4(data0, password) +
                    "\ndata1dec:" + Crypto.decryptRC4(data1, password) +
                    "\ndata2dec:" + Crypto.decryptRC4(data2, password));
        } catch (CryptoException e) {
            e.printStackTrace();
            assert false;
        }


        /*
 {
    "channel": "richkware",
    "data0": "BbVMwNMgUtglVgJx",
    "data1": "GKRb4M00IaM=",
    "data2": "NJ12_eEyaN8cf348RQf9_w=="
}
         */

    }

    @Test
    public void doDelete() {
    }

}