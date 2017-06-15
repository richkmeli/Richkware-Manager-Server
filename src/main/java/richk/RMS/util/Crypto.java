package richk.RMS.util;


import javax.xml.bind.DatatypeConverter;

public class Crypto {

    public static String Encrypt(String input, String key) {
        // encode input
        input = DatatypeConverter.printBase64Binary(input.getBytes());

        // Make sure the key is at least as long as the message
        String tmp = key;
        while (key.length() < input.length())
            key += tmp;

        // And now for the encryption part
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < input.length(); ++i) {
            output.append((char)(input.charAt(i) ^ key.charAt(i)));
        }

        // encode encrypted input
        String out = DatatypeConverter.printBase64Binary(output.toString().getBytes());
        return out;
    }

    public static String Decrypt(String input, String key) {
        // decode encrypted input
        byte[] decoded = DatatypeConverter.parseBase64Binary(input);
        input = new String(decoded);

        // Make sure the key is at least as long as the message
        String tmp = key;
        while (key.length() < input.length())
            key += tmp;

        // And now for the encryption part
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < input.length(); ++i) {
            output.append((char)(input.charAt(i) ^ key.charAt(i)));
        }

        // decode input
        decoded = DatatypeConverter.parseBase64Binary(output.toString());
        String out = new String(decoded);
        return out;
    }
}
