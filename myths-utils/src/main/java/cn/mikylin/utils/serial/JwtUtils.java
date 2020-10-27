package cn.mikylin.utils.serial;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;

/**
 * jwt utils
 *
 * @author mikylin
 * @date 20190714
 */
public class JwtUtils {

    /**
     * expire time
     */
    private static long DEFAULT_EXPIRE_TIME = 60 * 1000; // default 1 hour

    /**
     * cipher for HMAC256
     */
    private static String DEFAULT_CIPHER = "12345";

    public static void setCipher(String cipher) {
        JwtUtils.DEFAULT_CIPHER = cipher;
    }

    public static void setExpireTime(long time) {
        JwtUtils.DEFAULT_EXPIRE_TIME = time;
    }

    private static Algorithm algorithm;

    private static Algorithm createAlgorithm() {
        if(algorithm == null) {
            synchronized (JwtUtils.class) {
                if(algorithm == null)
                    algorithm = Algorithm.HMAC256(DEFAULT_CIPHER);
            }
        }
        return algorithm;
    }

    /**
     * verify the token is right
     *
     * @param token token
     * @param chaimKey key
     * @param chaimValue value
     * @return true - right , false - lost
     */
    public static boolean verify(String token, String chaimKey,String chaimValue) {
        try {
            Algorithm algorithm = createAlgorithm();
            JWTVerifier verifier
                    = JWT.require(algorithm)
                        .withClaim(chaimKey,chaimValue)
                        .build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * get the string type param in token
     *
     * @param token token
     * @param clainName param key
     * @return param value
     */
    public static String getStringClaim(String token,String clainName) {
        try {
            return getClaim(token,clainName).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * get the long type param in token
     *
     * @param token token
     * @param clainName param key
     * @return param value
     */
    public static Long getLongClaim(String token,String clainName) {
        try {
            return getClaim(token,clainName).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    private static Claim getClaim(String sign, String clainName){
        DecodedJWT jwt = JWT.decode(sign);
        return jwt.getClaim(clainName);
    }

    /**
     * create the token
     *
     * @param claims k-v
     * @return token
     */
    public static String token(Map<String,String> claims) {
        // 指定过期时间
        Date date = new Date(System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
        return token(claims,date);
    }

    /**
     * create the token
     *
     * @param claims k-v
     * @param expireTime expire time
     * @return token
     */
    public static String token(Map<String,String> claims,Date expireTime) {

        // 存入 键值对
        JWTCreator.Builder builder = JWT.create();
        claims.forEach((k,v) -> builder.withClaim(k,v));

        if(expireTime == null)
            return tokenWithOutExpireTime(builder);
        return tokenWithExpireTime(builder,expireTime);
    }


    private static String tokenWithOutExpireTime(JWTCreator.Builder builder) {
        Algorithm algorithm = createAlgorithm();
        return builder
                .sign(algorithm);
    }

    private static String tokenWithExpireTime(JWTCreator.Builder builder,Date expireTime) {
        Algorithm algorithm = createAlgorithm();
        return builder
                .withExpiresAt(expireTime)
                .sign(algorithm);
    }
}