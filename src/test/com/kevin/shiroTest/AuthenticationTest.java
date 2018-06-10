package com.kevin.shiroTest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */

public class AuthenticationTest {
    private static Logger logger = LoggerFactory.getLogger(com.kevin.shiroTest.AuthenticationTest.class);

    @Test
    public void testAuthentication() {
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        simpleAccountRealm.addAccount("Mark", "123456","admin","user");
        defaultSecurityManager.setRealm(simpleAccountRealm);
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);
        logger.info("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin","user");
        subject.logout();
        logger.info("isAuthenticated: " + subject.isAuthenticated());

    }
}
