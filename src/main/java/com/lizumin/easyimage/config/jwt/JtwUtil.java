package com.lizumin.easyimage.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 1:20 am
 * 4
 */
public class JtwUtil {
    private static final long EXPIRE_TIME = 30*60*1000;
    private static final String CLAIM_FILED = "username";

    /**
     * create Jwt token with username and password
     * @param key: a random key
     * @return Jwt token
     */
    public static String sign(String key,String username) {
//        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);

        Algorithm algorithm = Algorithm.HMAC256(key);
        // 附带username信息
        return JWT.create()
                .withClaim(CLAIM_FILED,username )
//                .withExpiresAt(date)
                .sign(algorithm);

    }

    /**
     * Get claim in the token
     * @return username
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(CLAIM_FILED).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     *
     * Use key to verify the jwt token
     *
     * @param token: jwt token
     * @param key:  crypto key in cache
     * @return boolean
     */
    public static boolean verify(String token,String key) throws JwtAuthenticationException {

        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT jwt = jwtVerifier.verify(token);

        } catch (SignatureVerificationException e) {
            throw new JwtAuthenticationException("the signature is invalid");
        }
        catch (TokenExpiredException e){
            throw new JwtAuthenticationException("The token has expired.");

        }
        catch (InvalidClaimException e){
            throw new JwtAuthenticationException("A claim contained a different value than the expected one.");

        }
        return true;
    }


    public static String decodeUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }
}