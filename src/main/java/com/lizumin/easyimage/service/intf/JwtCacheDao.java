package com.lizumin.easyimage.service.intf;

import java.util.Date;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 2:16 am
 * 4
 */
public interface JwtCacheDao {


    /**
     *
     * check if the jwt token in the cache
     *
     * @param username:  username
     * @return boolean
     */
    public boolean isJwtTokenAvailable(String username);



    /**
     *
     * delete the token cache
     *
     * @param username
     */
    public void deleteToken(String username);

    /**
     *
     * Refresh token
     *
     * @param username
     */
    public void refreshToken(String username);

    /**
     *
     * Get the secret key in cache
     *
     * @param  username
     * @return String: the secret key realting to the user
     */
    public String getSecrectKey(String username);

    /**
     *
     * store the token
     *
     * @param  username
     */
    public void storeJwt(String username,String token);

//    /**
//     *
//     * when the token expire, NOTICE THAT: it will refresh the token for 5 minutes
//     *
//     * @return int: how long the token will expire.
//     */
//    public long tokenExpireTime(String username);
}
