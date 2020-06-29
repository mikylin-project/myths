package cn.mikylin.myths.common.secret;

import cn.mikylin.myths.common.lang.StringUtils;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class RSAUtils {

    private static SecureRandom random = new SecureRandom();

    public static KeyPair keyPair() {
        return keyPair(1024);
    }

    public static KeyPair keyPair(int size) {
        return keyPair(size,random);
    }

    public static KeyPair keyPair(int size,SecureRandom r) {

        if(size < 96 || size > 1024)
            throw new IllegalArgumentException();

        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(size,r);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPrivateKey(KeyPair p) {
        return StringUtils.toString(p.getPrivate().getEncoded());
    }

    public static String getPublicKey(KeyPair p) {
        return StringUtils.toString(p.getPublic().getEncoded());
    }

    /**
     * RSA 公钥加密
     */
    public static String encrypt(String text,String publicKey) {

        Key k;
        try {
            // 封装 public key
            byte[] keyBytes = StringUtils.toBytes(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            k = KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doCipher(text,k);
    }

    public static String encrypt(String text,PublicKey publicKey) {
        return doCipher(text,publicKey);
    }


    /**
     * RSA 私钥解密
     */
    public static String decrypt(String text,String privateKey) {

        Key k;
        try {
            // 封装 public key
            byte[] keyBytes = StringUtils.toBytes(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            k = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doCipher(text,k);
    }

    public static String decrypt(String text,PrivateKey privateKey) {
        return doCipher(text,privateKey);
    }

    /**
     * RSA 公钥加密
     */
    private static String doCipher(String text, Key k) {

        try {
            // 创建 cipher
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,k);

            // 加密
            byte[] textBytes = cipher.doFinal(StringUtils.toBytes(text));
            return StringUtils.toString(textBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}