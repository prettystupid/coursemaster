package encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

public class Encrypter {

    private static String algorithm = "DESede";
    private Key key = null;
    private static Cipher cipher = null;

    public Encrypter() {
        try {
            key = KeyGenerator.getInstance(algorithm).generateKey();
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

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
