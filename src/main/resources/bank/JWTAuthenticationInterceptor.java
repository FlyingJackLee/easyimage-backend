package bank;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/15 5:04 am
 * 4
 */
public class JWTAuthenticationInterceptor implements HandlerInterceptor {
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //get token from request
        String token = request.getHeader("token");

        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //if the method do not need token
        if (method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);

            if (passToken.required()){
                return true;
            }
        }

        if (method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);

            if (userLoginToken.required()){
                //authenticate begin
                if (token == null){
                    throw new RuntimeException("Cannot find token, please login");
                }

                //get user id in token;
                long userId;
                try {
                    userId = Long.parseLong(JWT.decode(token).getAudience().get(0));
                }
                catch (JWTDecodeException e){
                    throw new RuntimeException("Token format error.");
                }

                User user = userRepository.findUserById(userId);
                if (user == null){
                    throw new RuntimeException("User not found.");
                }

                //check the token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();

                try {
                    jwtVerifier.verify(token);
                }catch (JWTVerificationException e){
                    throw new RuntimeException("Token not match");
                }

                return true;

            }

        }
        return true;
    }





    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
