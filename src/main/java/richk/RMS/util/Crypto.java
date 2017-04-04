package richk.RMS.util;

public class Crypto {

    public static String EncryptDecrypt(String input, int key) {
        String output = null;

        for (int i = 0; i < input.length(); ++i) {
            output += input.charAt(i) ^ key;
        }

        return output;
    }
}
