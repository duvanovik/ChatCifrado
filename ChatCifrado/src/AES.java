import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AES {
    private static final String ALGORITHM = "AES";
    private final Key key;

    public AES(byte[] keyBytes) {
        this.key = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes());
    }

    public String decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(data));
    }
}
