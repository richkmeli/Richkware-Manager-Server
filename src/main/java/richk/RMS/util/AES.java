package richk.RMS.util;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES {
    private static int keySize = 256;
    //private static String algorithm = "AES/CBC/PKCS5Padding";
    private static String algorithm = "AES";

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = null;
        keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);

        return keyGenerator.generateKey();
    }

    public static byte[] encrypt(byte[] plaintext, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(plaintext);
    }

    public static byte[] decrypt(byte[] ciphertext, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(ciphertext);
    }

}
