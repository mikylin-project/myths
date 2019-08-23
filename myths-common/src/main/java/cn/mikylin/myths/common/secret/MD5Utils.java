package cn.mikylin.myths.common.secret;

import cn.mikylin.myths.common.NioLocal;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 utils
 * @author mikylin
 * @date 20190714
 */
public final class MD5Utils {

    private static char[] HEX_DIGITS;

    static {
         HEX_DIGITS = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8',
                                    '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }

    /**
     * get the MessageDigest util
     */
    private static MessageDigest getMessageDigest(){
        try{
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }


    /**
     * create the md5 string by bytes
     */
    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }


    /**
     * create the md5 string
     */
    public static String bit32(String origin) {

        MessageDigest digest = getMessageDigest();
        digest.update(origin.getBytes());
        byte[] messageDigest = digest.digest();
        return toHexString(messageDigest);
    }

    public static String bit32LowCase(String origin) {
        return bit32(origin).toLowerCase();
    }

    public static String bit16(String origin) {
        return bit32(origin).substring(8, 24);
    }

    public static String bit16LowCase(String origin) {
        return bit16(origin).toLowerCase();
    }

    /**
     * get file‘s md5
     */
    public static String fileMD5Bit32(File file) {
        byte[] b = getMessageDigest().digest(NioLocal.fileToBytes(file));
        return toHexString(b);
    }

}
