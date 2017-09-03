package richk.RMS.util;

import java.util.Random;

public class RandomStringGenerator {
    public static String GenerateString(int lenght) {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int alphabetLength = alphabet.length();

        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < lenght; ++i) {
            result.append(alphabet.charAt(random.nextInt(alphabetLength)));
        }

        return result.toString();
    }
}
