package application.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

public class Encrypter {

    private static String algorithm = "DESede";
    private Key key = null;
    private static Cipher cipher = null;

    public Encrypter(Key key) {
        try {
            this.key = key;
            cipher = Cipher.getInstance(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] input){
        byte[] result = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = cipher.doFinal(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
