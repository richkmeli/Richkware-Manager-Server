package richk.RMS.util;

public class Crypto {

    public static String Encrypt(String input, String key) {
        input += "#@#@#@";
        if(input.length() < key.length()){
            while(input.length() == key.length()){
                input += "0";
            }
        }

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

        output.replace(0,output.length(), output.substring(0,output.indexOf("#@#@#@")));
        return output.toString();
    }
}
