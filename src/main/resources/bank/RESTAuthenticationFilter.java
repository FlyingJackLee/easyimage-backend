package bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/15 2:04 am
 * 4
 */
public class RESTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     *
     * Use json/RestFul api login to replace default form login
     *
     * @param request:
     * @param response:
     * @return org.springframework.security.core.Authentication
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        Map result = null;
        try {
            result = mapper.readValue(request.getReader(),Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = (String)result.get("username");
        String password = (String)result.get("password");

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);

    }
}
