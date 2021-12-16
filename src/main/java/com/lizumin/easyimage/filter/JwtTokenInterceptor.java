package com.lizumin.easyimage.filter;

import com.lizumin.easyimage.annos.RequiredJwtToken;
import com.lizumin.easyimage.config.jwt.JtwToken;
import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import org.apache.catalina.core.ApplicationContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 6:52 am
 * 4
 */
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(JWTSetting.TOKEN_HEADER);

        //skip none method
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        final HandlerMethod handlerMethod = (HandlerMethod) handler;

        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(RequiredJwtToken.class)){

            //prevent request without token
            if (token == null || "".equals(token)){
                throw new JwtAuthenticationException("Valid Token.");
            }

            RequiredJwtToken requiredJwtToken = method.getAnnotation(RequiredJwtToken.class);

            JtwToken jtwToken = new JtwToken(token);

            Subject currentUser = SecurityUtils.getSubject();

            currentUser.login(jtwToken);

            if (currentUser.isAuthenticated()){
                return true;
            } 
            else {
                throw new JwtAuthenticationException("Authenticate fail.");
            }
        }

        return true;
    }





}

