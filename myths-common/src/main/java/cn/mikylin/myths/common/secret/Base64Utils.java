package cn.mikylin.myths.common.secret;

import cn.mikylin.myths.common.lang.StringUtils;
import java.util.Base64;

/**
 * summarized by jdk8 Base64.
 *
 * @author mikylin
 * @date 20190714
 */
public final class Base64Utils {

    /**
     * url encode.
     *
     * @param url base url
     * @return string
     */
    public static String encodeUrl(String url) {
        return encode(Base64.getUrlEncoder(),url);
    }

    /**
     * url decode.
     *
     * @param encodeString encode string
     */
    public static String decodeUrl(String encodeString) {
        return decode(Base64.getUrlDecoder(),encodeString);
    }

    /**
     * mime encode
     * @param url base url
     *            @return string
     */
    public static String encodeMime(String url) {
        return encode(Base64.getMimeEncoder(),url);
    }

    /**
     * mime decode
     *
     * @param encodeString encode string
     * @return string
     */
    public static String decodeMime(String encodeString) {
        return decode(Base64.getMimeDecoder(),encodeString);
    }

    /**
     * base encode.
     *
     * @param url base url
     * @return string
     */
    public static String encode(String url) {
        return encode(Base64.getEncoder(),url);
    }

    public static String encode(byte[] url) {
        return encode(Base64.getEncoder(),url);
    }

    /**
     * base decode
     *
     * @param encodeString encode string
     * @return string
     */
    public static String decode(String encodeString) {
        return decode(Base64.getDecoder(),encodeString);
    }

    public static String decode(byte[] bs) {
        return decode(Base64.getDecoder(),bs);
    }


    /**
     * 加密
     */
    private static String encode(Base64.Encoder encoder,String url) {
        return encode(encoder, StringUtils.toBytes(url));
    }

    private static String encode(Base64.Encoder encoder,byte[] url) {
        return encoder.encodeToString(url);
    }

    /**
     * 解密
     */
    private static String decode(Base64.Decoder decoder,String encodeString) {
        return StringUtils.toString(decoder.decode(encodeString));
    }

    private static String decode(Base64.Decoder decoder,byte[] encodeByte) {
        return StringUtils.toString(decoder.decode(encodeByte));
    }


}
