package cn.itcast.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-04 15:48:13
 */
public class ShiroTest01
{
    @Test
    public void testLogin() {
        // 1.加载ini配置文件创建SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-test-1.ini");
        // 2.获取securityManager
        SecurityManager securityManager = factory.getInstance();
        // 3.将securityManager绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);

        // 4.创建主体(此时的主体还为经过认证)
        Subject subject = SecurityUtils.getSubject();

        /**
         * 模拟登录，和传统等不同的是需要使用主体进行登录
         */
        // 5.构造主体登录的凭证（即用户名/密码）
        // 第一个参数：登录用户名，第二个参数：登录密码
        String username = "zhangsan";
        String password = "123456";
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        // 6.主体登录
        subject.login(token);
        // 7.验证是否登录成功
        System.out.println("用户是否登录成功=" + subject.isAuthenticated());
        // 8.登录成功获取数据
        // getPrincipal 获取登录成功的安全数据
        System.out.println(subject.getPrincipal());
    }
}
