package com.lizumin.easyimage.config.jwt;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.Role;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.JwtCacheDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 1:05 am
 * 4
 */
public class JwtRealm extends AuthorizingRealm {

    private UserRepository userRepository;

    private JwtCacheDao jwtCacheDao;

    /**
     *
     * How the give a user authorizations
     *
     * @param principalCollection:  Principals from a subject
     * @return org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JtwUtil.getUsername(principalCollection.toString());

        User user = userRepository.findUserByUsername(username);

        if (user != null){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (Role role:user.getRoles()){
                info.addRole(role.getName());
                info.addStringPermissions(role.getPermissions());
            }
            return info;
        }
        else {
            return null;
        }

    }
    /**
     *
     * a simple jwt token authentication process
     *
     * @param token:  jwt Token
     * @return org.apache.shiro.authc.AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwtToken = (String) token.getCredentials();

        String username = (String) token.getPrincipal();


        if (username ==null || "".equals(username)){
            throw new JwtAuthenticationException("Invalid token.");
        }

        //check if there is a jwt token cache relating to the user.
        if (jwtCacheDao.isJwtTokenAvailable(username)){
            String key = jwtCacheDao.getSecrectKey(username);
            if (JtwUtil.verify(jwtToken,key)){
                this.jwtCacheDao.refreshToken(username);
                return new SimpleAuthenticationInfo(username,jwtToken,getName());
           }
           else {
                throw new JwtAuthenticationException("Bad Authentication.");
            }
        }
        else {
            throw new JwtAuthenticationException("No Authentication, please login.");
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JtwToken;
    }

    @Autowired
    public void setJwtCacheDao(JwtCacheDao jwtCacheDao) {
        this.jwtCacheDao = jwtCacheDao;
    }



    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
