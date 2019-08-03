package cn.mikylin.myths.common.secret;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * summarized by jdk8 Base64
 * @author mikylin
 * @date 20190714
 */
public final class Base64Utils {

    /**
     * url encode
     * @param url base url
     */
    public String encodeUrl(String url) {
        return encode(Base64.getUrlEncoder(),url);
    }

    /**
     * url decode
     * @param encodeString encode string
     */
    public String decodeUrl(String encodeString) {
        return decode(Base64.getUrlDecoder(),encodeString);
    }

    /**
     * mime encode
     * @param url base url
     */
    public String encodeMime(String url) {
        return encode(Base64.getMimeEncoder(),url);
    }

    /**
     * mime decode
     * @param encodeString encode string
     */
    public String decodeMime(String encodeString) {
        return decode(Base64.getMimeDecoder(),encodeString);
    }

    /**
     * base encode
     * @param url base url
     */
    public String encode(String url) {
        return encode(Base64.getEncoder(),url);
    }

    /**
     * base decode
     * @param encodeString encode string
     */
    public String decode(String encodeString) {
        return decode(Base64.getDecoder(),encodeString);
    }




    private String encode(Base64.Encoder encoder,String url) {
        try {
            // 加密的过程
            return encoder.encodeToString(url.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("string to bytes exception");
        }
    }

    private String decode(Base64.Decoder decoder,String encodeString) {
        try {
            // 解密的过程
            return new String(decoder.decode(encodeString),"UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("bytes to string exception");
        }
    }


}
