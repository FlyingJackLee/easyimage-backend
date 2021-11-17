package com.lizumin.easyimage.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 12:18 am
 * 4
 */
public class JtwToken implements AuthenticationToken {
    private final String token;

    public JtwToken(String token){
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return JtwUtil.getUsername(token);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

}
