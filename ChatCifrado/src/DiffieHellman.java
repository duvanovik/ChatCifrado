import javax.crypto.KeyAgreement;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

public class DiffieHellman {
    private KeyPair keyPair;
    private KeyAgreement keyAgree;

    public DiffieHellman() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(2048);
        this.keyPair = keyPairGen.generateKeyPair();
        this.keyAgree = KeyAgreement.getInstance("DH");
        this.keyAgree.init(this.keyPair.getPrivate());
    }

    public byte[] generateSharedSecret(byte[] receivedPubKeyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(receivedPubKeyBytes);
        PublicKey receivedPubKey = keyFactory.generatePublic(x509KeySpec);
        this.keyAgree.doPhase(receivedPubKey, true);
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        hash.update(keyAgree.generateSecret());
        byte[] aesKey = new byte[32]; // 256 bits
        System.arraycopy(hash.digest(), 0, aesKey, 0, aesKey.length);
        return aesKey;
    }

    public byte[] getEncodedPublicKey() {
        return keyPair.getPublic().getEncoded();
    }
}
