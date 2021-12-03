package com.lizumin.easyimage.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/3 2:24 AM
 * 4
 */
@Component
public class ExceptionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request,response);
        }
        catch (Exception e){
            request.setAttribute("filter.error",e);
            request.getRequestDispatcher("/api/error/throw").forward(request,response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }
}
