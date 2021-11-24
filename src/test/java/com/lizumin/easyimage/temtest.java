package com.lizumin.easyimage;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 7:46 pm
 * 4
 */
public class temtest {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Mzc3ODM2NzAsInVzZXJuYW1lIjoidGVzdCJ9.4KpGfrrE89uOeFbRMPUDyP7UIF3vge0Ie4DewwbbRzM";
        DecodedJWT decodedJWT = JWT.decode(token);

        System.out.println(decodedJWT.getPayload());
        System.out.println(decodedJWT.getToken());

        System.out.println(decodedJWT.getAudience());
        System.out.println(decodedJWT.getHeader());
        System.out.println(decodedJWT.getClaim("username"));
        String username = decodedJWT.getClaim("username").asString();
        System.out.println(username);

    }
}
