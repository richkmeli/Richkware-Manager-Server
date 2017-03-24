package richk.RMS.util;

<<<<<<< HEAD
public class Crypto {
=======
public class Cripto {
>>>>>>> branch 'master' of https://github.com/richkmeli/Richkware-Manager-Server.git

	
	public static String EncryptDecrypt(String input, int key) {
		String output = null;

		for(int i = 0; i < input.length(); ++i){
			output += input.charAt(i) ^ key;
		}

		return output;
	}
}
