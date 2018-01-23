package richk.RMS.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static KeyPair GetGeneratedKeyPairRSA() throws CryptoException {
        try {
            return RSA.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static List<Object> KeyExchangeAESRSA(KeyPair keyPairServer, String kpubClient) throws CryptoException {

        // String to public key
        KeyFactory keyFactory = null;
        PublicKey pubKeyClient;
        SecretKey AESsecretKey;
        try {
            /*byte[] publicBytes = DatatypeConverter.parseBase64Binary(kpubClient);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            keyFactory = KeyFactory.getInstance("RSA");
            pubKeyClient = keyFactory.generatePublic(keySpec);*/

            pubKeyClient = loadPublicKey(kpubClient);

            AESsecretKey = AES.generateKey();
        } catch (Exception e) {
            throw new CryptoException(e);
        }

        PublicKey RSApublicKeyServer = keyPairServer.getPublic();
        PrivateKey RSAprivateKeyServer = keyPairServer.getPrivate();

        String signatureAESsecretKey = null;
        byte[] encryptedAESsecretKey = null;
        try {
            // sign AES key
            signatureAESsecretKey = RSA.sign(AESsecretKey.getEncoded(), RSAprivateKeyServer);
            encryptedAESsecretKey = RSA.encrypt(AESsecretKey.getEncoded(), pubKeyClient);
        } catch (Exception e) {
            throw new CryptoException(e);
        }

        List<Object> results = new ArrayList<Object>();

        String encryptedAESsecretKeyS = DatatypeConverter.printHexBinary(encryptedAESsecretKey);
        signatureAESsecretKey = DatatypeConverter.printHexBinary(signatureAESsecretKey.getBytes());
        String RSApublicKeyServerS = null;
        try {
            RSApublicKeyServerS = savePublicKey(RSApublicKeyServer);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(e);
        }

        results.add(new KeyExchangePayload(encryptedAESsecretKeyS, signatureAESsecretKey, RSApublicKeyServerS, null));
        results.add(AESsecretKey);

        return results;
    }

    public static SecretKey GetAESKeyFromKeyExchange(KeyExchangePayload keyExchangePayload, PrivateKey RSAprivateKeyClient) throws CryptoException {
        byte[] encryptedAESsecretKey = DatatypeConverter.parseHexBinary(keyExchangePayload.encryptedAESsecretKey);
        String signatureAESsecretKey = new String(DatatypeConverter.parseHexBinary(keyExchangePayload.signatureAESsecretKey));

        PublicKey kpubServer = null;
        try {
            kpubServer = loadPublicKey(keyExchangePayload.kpubServer);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(e);
        }

        byte[] AESsecretKey = null;
        try {
            AESsecretKey = RSA.decrypt(encryptedAESsecretKey, RSAprivateKeyClient);
            if (!(RSA.verify(AESsecretKey, signatureAESsecretKey, kpubServer))) {
                throw new CryptoException(new Exception("Failed to verify signature of message received. GetAESKeyFromKeyExchange()"));
            }
        } catch (Exception e) {
            throw new CryptoException(e);
        }

        return new SecretKeySpec(AESsecretKey, 0, AESsecretKey.length, "AES");

    }

    public static String EncryptAES(String input, SecretKey key) throws CryptoException {
        // encode input
        input = DatatypeConverter.printBase64Binary(input.getBytes());

        byte[] ciphertext = null;
        try {
            ciphertext = AES.encrypt(input.getBytes(), key);
        } catch (Exception e) {
            throw new CryptoException(e);
        }


        // encode encrypted input
        //return DatatypeConverter.printBase64Binary(ciphertext.getBytes());
        return DatatypeConverter.printHexBinary(ciphertext);
    }

    public static String DecryptAES(String input, SecretKey key) throws CryptoException {
        // decode encrypted input
        //byte[] decoded = DatatypeConverter.parseBase64Binary(input);
        byte[] decoded = DatatypeConverter.parseHexBinary(input);

        byte[] plaintext = null;
        try {
            plaintext = AES.decrypt(decoded, key);
        } catch (Exception e) {
            throw new CryptoException(e);
        }


        // decode input
        decoded = DatatypeConverter.parseBase64Binary(new String(plaintext));
        return new String(decoded);
    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = DatatypeConverter.parseHexBinary(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }


    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = DatatypeConverter.parseHexBinary(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }

    public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
                PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = DatatypeConverter.printHexBinary(packed);

        Arrays.fill(packed, (byte) 0);
        return key64;
    }


    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
                X509EncodedKeySpec.class);
        return DatatypeConverter.printHexBinary(spec.getEncoded());
    }

    public static String HashSHA256(String input) {
        // encode input
        //input = DatatypeConverter.printBase64Binary(input.getBytes());
        //input = DatatypeConverter.printHexBinary(input.getBytes());

        MessageDigest digest = null;
        byte[] hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // encode encrypted input
        //return DatatypeConverter.printBase64Binary(ciphertext);
        return DatatypeConverter.printHexBinary(hash);
    }

}
