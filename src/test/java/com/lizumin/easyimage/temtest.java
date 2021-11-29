package com.lizumin.easyimage;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 7:46 pm
 * 4
 */
public class temtest {
    public static void main(String[] args) {
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Mzc3ODM2NzAsInVzZXJuYW1lIjoidGVzdCJ9.4KpGfrrE89uOeFbRMPUDyP7UIF3vge0Ie4DewwbbRzM";
//        DecodedJWT decodedJWT = JWT.decode(token);
//
//        System.out.println(decodedJWT.getPayload());
//        System.out.println(decodedJWT.getToken());
//
//        System.out.println(decodedJWT.getAudience());
//        System.out.println(decodedJWT.getHeader());
//        System.out.println(decodedJWT.getClaim("username"));
//        String username = decodedJWT.getClaim("username").asString();
//        System.out.println(username);
//        System.out.println(new Date());
//
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("X0zF85uGbX")).build();
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Mzc4MDQwNDUsInVzZXJuYW1lIjoidGVzdCJ9.nrMcUHEDuf7BWBaB0l49Hmeob8SK5kza7wKRv6Q7b9k";
//        DecodedJWT decodedJWT = jwtVerifier.verify(token);
//        System.out.println(decodedJWT);

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Mzc4MTEwNjksInVzZXJuYW1lIjoidGVzdCJ9.9xG8vHq9O3Dys6dwv7cGSJXd2b4rgaHmLkOjWDrxv9s";
        try {
            Algorithm algorithm = Algorithm.HMAC256("K4ky8mGAFz");
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT jwt = jwtVerifier.verify(token);
            System.out.println("yes yes");
        }
        catch (JWTVerificationException e){
            System.out.println("????");
        }


    }
}
