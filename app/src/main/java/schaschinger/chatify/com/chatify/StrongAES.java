package schaschinger.chatify.com.chatify;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class StrongAES {
    private static Key key = null;
    private static final String algorithm = "AES";
    private static final int KEYSIZE = 128;

    public StrongAES() {
    }

    public static void generateNewKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey aesKey = keyGen.generateKey();
        key = aesKey;
    }

    public static String encryptString(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        byte[] utf8 = null;

        try {
            utf8 = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        byte[] encrypted = cipher.doFinal(utf8);
        String result = Base64.encodeToString(encrypted, encrypted.length);
        System.out.println("Encrypted : " + result);
        return result;
    }

    public static String decryptByteArray(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        String decrypted = null;
        byte[] decodedData = Base64.decode(encrypted, encrypted.length);
        Object utf8 = null;

        byte[] utf81;
        try {
            utf81 = cipher.doFinal(decodedData);
        } catch (Exception var7) {
            return null;
        }

        try {
            decrypted = new String(utf81, "UTF-8");
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        return decrypted;
    }

    public static Key getKey() {
        return key;
    }

    public static void setKey(Key key) {
        key = key;
    }
}