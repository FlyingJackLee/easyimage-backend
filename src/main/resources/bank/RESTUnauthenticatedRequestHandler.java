package bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/15 2:45 am
 * 4
 */
public class RESTUnauthenticatedRequestHandler implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        System.out.println(authException);
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode rootNode = mapper.createObjectNode();
//        rootNode.put("message","No authentication");
//
//        response.getWriter().write( mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));

    }
}
