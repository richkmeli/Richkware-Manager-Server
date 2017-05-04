package richk.RMS.util;

public class Crypto {

    public static String Encrypt(String input, String key) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0, j = 0; i < input.length(); ++i) {
            output.append((char)(input.charAt(i) ^ key.charAt(j)));

            if(j == key.length()-1)
                j = 0;
            else ++j;
        }

        return output.toString();
    }

    public static String Decrypt(String input, String key) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0, j = 0; i < input.length(); ++i) {
            output.append((char)(input.charAt(i) ^ key.charAt(j)));

            if(j == key.length()-1)
                j = 0;
            else ++j;
        }

        return output.toString();
    }
}