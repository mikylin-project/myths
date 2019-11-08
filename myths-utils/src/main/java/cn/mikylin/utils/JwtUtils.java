package cn.mikylin.utils;

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
     * 设置 token 的过期时间，单位毫秒，此处设置为 1 小时
     */
    private static final long DEFAULT_EXPIRE_TIME = 60 * 1000;

    /**
     * HMAC256 模式下生成 token 的秘钥
     */
    private static String SALT = "12345";


    private static Algorithm createAlgorithm(){
        Algorithm algorithm = Algorithm.HMAC256(SALT);
        return algorithm;
    }


    /**
     * 校验token是否正确
     *
     * @param token    密钥
     * @param chaimKey 效验的 key
     * @param chaimValue 效验的 value
     * @return
     */
    public static boolean verify(String token, String chaimKey,String chaimValue) {
        try {
            Algorithm algorithm = createAlgorithm();
            JWTVerifier verifier
                    = JWT.require(algorithm)
                        .withClaim(chaimKey,chaimValue)
                        .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 String 类型的 jwt 参数
     */
    public static String getStringClaim(String token,String clainName) {
        try {
            return getClaim(token,clainName).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取 Long 类型的 jwt 参数
     */
    public static Long getLongClaim(String token,String clainName) {
        try {
            return getClaim(token,clainName).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    private static Claim getClaim(String token, String clainName){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(clainName);
    }

    /**
     * 生成签名
     */
    public static String sign(Map<String,String> claims) {
        // 指定过期时间
        Date date = new Date(System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

        // 存入 键值对
        Algorithm algorithm = createAlgorithm();
        JWTCreator.Builder builder = JWT.create();
        claims.forEach((k,v) -> builder.withClaim(k,v));

        return builder
                .withExpiresAt(date)
                .sign(algorithm);
    }

}
