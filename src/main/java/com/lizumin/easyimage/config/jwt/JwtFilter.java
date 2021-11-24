package com.lizumin.easyimage.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 12:19 am
 * 4
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {


    /**
     *
     * check the Authorization header to confirm if the user needs login
     *
     * @param request
     * @param response
     * @return boolean : if the user needs login
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");

        JtwToken token = new JtwToken(authorization);

        //give the token to the jwt realm
        getSubject(request,response).login(token);


        //if no Exception throws in the last login
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        if (isLoginAttempt(request,response)){
            try {
                executeLogin(request, response);
            }  catch (Exception e) {
            throw new JwtAuthenticationException("Login failed.");
        }
        }
        return true;
    }

    /**
     *
     * create a jwt token
     *
     * @param request:
     * @param response:
     * @return org.apache.shiro.authc.AuthenticationToken
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String authorization = getAuthzHeader(request);
        return new JtwToken(authorization);
    }

}
