package com.lizumin.easyimage.config.jwt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 3:29 am
 * 4
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    /**
     *
     * tell shiro how to use different realm(by type of token)
     *
     * @param realms: all registered realms
     * @param token: token submitted
     * @return org.apache.shiro.authc.AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        assertRealmsConfigured();


        Realm uniqueRealm = null;
        for (Realm realm :realms) {
            if (realm.supports(token)){
                uniqueRealm = realm;
                break;
            }
        }

        if (uniqueRealm == null) {
            throw new UnsupportedOperationException();
        }

        return uniqueRealm.getAuthenticationInfo(token);
    }
}
