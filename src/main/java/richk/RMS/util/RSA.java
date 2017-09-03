package richk.RMS.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.security.*;

public class RSA {
    private static int keySize = 2048;
    private static String encryptDecryptAlgorithm = "RSA";
    private static String signVerifyAlgorithm = "SHA256withRSA";

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = null;
        keyPairGenerator = KeyPairGenerator.getInstance(encryptDecryptAlgorithm);
        keyPairGenerator.initialize(keySize);


        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encrypt(byte[] plaintext, PublicKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(encryptDecryptAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    public static byte[] decrypt(byte[] ciphertext, PrivateKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(encryptDecryptAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(ciphertext);
    }

    public static String sign(byte[] plainText, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance(signVerifyAlgorithm);
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText);

        return DatatypeConverter.printBase64Binary(privateSignature.sign());
    }

    public static boolean verify(byte[] plainText, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature publicSignature = Signature.getInstance(signVerifyAlgorithm);
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText);

        return publicSignature.verify(DatatypeConverter.parseBase64Binary(signature));
    }
}