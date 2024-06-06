package com.ihrm.company.config;

import com.google.common.collect.Maps;
import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.common.shiro.session.IhrmSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ShiroConfiguration
{
    /**
     * 自定义领域
     *
     * @return {@link IhrmRealm }
     */
    @Bean
    public IhrmRealm getRealm() {
        return new IhrmRealm();
    }

    /**
     * 安全管理器
     *
     * @return {@link SecurityManager }
     */
    @Bean
    public SecurityManager securityManager() {
        // 安全管理器
        final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 会话管理器
        securityManager.setSessionManager(sessionManager());
        // 缓存管理器
        securityManager.setCacheManager(cacheManager());
        // 领域
        securityManager.setRealm(getRealm());
        return securityManager;
    }

    /**
     * Shiro过滤器工厂类
     *
     * @param securityManager 安全管理器
     * @return {@link ShiroFilterFactoryBean }
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        final ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/autherror?code=1");
        factoryBean.setUnauthorizedUrl("/autherror?code=2");

        // anon, authc, authcBasic, user 是第一组认证过滤器，perms, port, rest, roles, ssl 是第二组授权过滤器
        // 要通过授权过滤器，就先要完成登陆认证操作（即先要完成认证才能前去寻找授权) 才能走第二组授权器（例如访问需要 roles 权限的 url，如果还没有登陆的话，会直接跳转到shiroFilterFactoryBean.setLoginUrl(); 设置的 url ）
        final Map<String, String> filterMap = Maps.newHashMap();
        filterMap.put("/sys/login", "anon");                // 匿名访问
        filterMap.put("/autherror", "anon");                // 匿名访问
        filterMap.put("/**", "authc");                      // 认证访问

        factoryBean.setFilterChainDefinitionMap(filterMap);
        return factoryBean;
    }

    /**
     * 配置Shiro注解支持
     *
     * @param securityManager 安全管理器
     * @return {@link AuthorizationAttributeSourceAdvisor }
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        final AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    // =====================================================Redis====================================================

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * Redis 管理器
     *
     * @return {@link RedisManager }
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * Redis 缓存管理器
     *
     * @return {@link RedisCacheManager }
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * Redis 会话 DAO
     *
     * @return {@link RedisSessionDAO }
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 默认 Web 会话管理器
     *
     * @return {@link DefaultWebSessionManager }
     */
    public DefaultWebSessionManager sessionManager() {
        final IhrmSessionManager sessionManager = new IhrmSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

}
