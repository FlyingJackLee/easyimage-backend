package com.lizumin.easyimage.config;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.config.jwt.CustomModularRealmAuthenticator;
import com.lizumin.easyimage.config.jwt.JwtFilter;
import com.lizumin.easyimage.config.jwt.JwtRealm;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import java.util.*;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/16 7:43 pm
 * 4
 */
@Configuration
public class ShiroConfig {
    private  UserRepository userRepository;

    private Environment environment;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(){
        final FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(this.corsFilter());
        registrationBean.setOrder(0);

        return registrationBean;
    }


    private CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(this.environment.getProperty("cors.domain")));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }




    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        CustomModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }


    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());


        List<Realm> realms = new ArrayList<>(2);
        realms.add(userRealm());
        realms.add(jwtRealm());

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        securityManager.setRealms(realms);


        return securityManager;

    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);

        factoryBean.setSecurityManager(securityManager);

        Map<String, String> filterRuleMap = new HashMap<>();
        filterRuleMap.put("/img/**","anno");
        filterRuleMap.put("/**", "jwt");
        filterRuleMap.put("/login", "authc");
        return factoryBean;
    }




    @Bean
    public UserRealm userRealm(){
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(credentialsMatcher());
        realm.setUserRepository(userRepository);
        return realm;
    }

    @Bean
    public JwtRealm jwtRealm(){
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCachingEnabled(true);
        return new JwtRealm();
    }

    @Bean
    public DefaultSubjectDAO subjectDAO() {
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return defaultSubjectDAO;
    }

    public SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }


    @Bean(name = "credentialsMatcher")
    public PasswordMatcher credentialsMatcher() {
        final PasswordMatcher credentialsMatcher = new PasswordMatcher();
        credentialsMatcher.setPasswordService(passwordService());
        return credentialsMatcher;
    }

    @Bean(name = "passwordService")
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }

    @Bean
    public Authorizer authorizer(){
        return new ModularRealmAuthorizer();
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    //    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//        chainDefinition.addPathDefinition("/**", "anon"); // all paths are managed via annotations
//
//
//        // or allow basic authentication, but NOT require it.
//        // chainDefinition.addPathDefinition("/**", "authcBasic[permissive]");
//        return chainDefinition;
//    }

    @Bean
    public Authenticator authenticator(){
        return new CustomModularRealmAuthenticator();
    }


//    /**
//     * Auto annotation with cglib
//     */
//    @Bean
//    @DependsOn("lifecycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        // https://zhuanlan.zhihu.com/p/29161098
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
