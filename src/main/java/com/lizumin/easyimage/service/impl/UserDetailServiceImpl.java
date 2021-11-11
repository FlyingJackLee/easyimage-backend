package com.lizumin.easyimage.service.impl;

import com.lizumin.easyimage.Dao.UserProfileRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.model.entity.UserProfile;
import com.lizumin.easyimage.service.intf.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * This class is provided to AuthenticationConfiguration
 * so the security is able to authentication.
 *
 *
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 1:08 am
 * 4
 */
@Service
@Primary
public class UserDetailServiceImpl implements UserDetailsService,UserDao {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user does not exist");
        }
        return user;
    }

    @Override
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
         userRepository.save(user).getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setLocale(Locale.ENGLISH);
        userProfile.setUser(user);

        userProfileRepository.save(userProfile);

        return user;

    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
}
