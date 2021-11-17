package com.lizumin.easyimage.config;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.Role;
import com.lizumin.easyimage.model.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/16 9:22 pm
 * 4
 */
public class UserRealm extends AuthorizingRealm {


    private UserRepository userRepository;

    /**
     *
     * How the shiro authenticate user
     *
     * @param authenticationToken:  TODO
     * @return org.apache.shiro.authc.AuthenticationInfo TODO
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //authenticate by dao
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userRepository.findUserByUsername(token.getUsername());

        if (user != null){
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getId(),user.getPassword(),getName());
            return info;
        }
        else {
            return null;
        }
    }

    /**
     *
     * How the give a user authorizations
     *
     * @param principalCollection:  Principals from a subject
     * @return org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Long userId = (Long)principalCollection.fromRealm(getName()).iterator().next();

        User user = userRepository.findUserById(userId);

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

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
