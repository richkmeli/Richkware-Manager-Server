package richk.RMS.util;

import javax.xml.bind.DatatypeConverter;

public class Crypto {

    public static String EncryptRC4(String input, String key) {
        // encode input
        input = DatatypeConverter.printBase64Binary(input.getBytes());
        //input = DatatypeConverter.printHexBinary(input.getBytes());

        RC4 rc4 = new RC4(key.getBytes());
        byte[] ciphertext = rc4.encrypt(input.getBytes());

        // encode encrypted input
        //return DatatypeConverter.printBase64Binary(ciphertext);
        return DatatypeConverter.printHexBinary(ciphertext);
    }

    public static String DecryptRC4(String input, String key) {
        // decode encrypted input
        //byte[] decoded = DatatypeConverter.parseBase64Binary(input);
        byte[] decoded = DatatypeConverter.parseHexBinary(input);

        RC4 rc4 = new RC4(key.getBytes());
        byte[] plaintext = rc4.decrypt(decoded);
        String plaintextS = new String(plaintext);

        // decode input
        decoded = DatatypeConverter.parseBase64Binary(plaintextS);
        //decoded = DatatypeConverter.parseHexBinary(plaintextS);
        return new String(decoded);
    }

    public static String EncryptAES(String input, String key) {
        // encode input
        input = DatatypeConverter.printBase64Binary(input.getBytes());

        AES aes = new AES(key);
        String ciphertext = null;
        try {
            ciphertext = aes.encrypt(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // encode encrypted input
        return DatatypeConverter.printBase64Binary(ciphertext.getBytes());
    }

    public static String DecryptAES(String input, String key) {
        // decode encrypted input
        byte[] decoded = DatatypeConverter.parseBase64Binary(input);
        input = new String(decoded);

        AES aes = new AES(key);
        String plaintext = null;
        try {
            plaintext = aes.decrypt(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // decode input
        decoded = DatatypeConverter.parseBase64Binary(plaintext);
        return new String(decoded);
    }

}
