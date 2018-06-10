package com.kevin.shiroTest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */
public class CustomRealmTest {
    private static Logger logger = LoggerFactory.getLogger(com.kevin.shiroTest.CustomRealmTest.class);

    @Test
    public void testCustomRealm() {
        CustomRealm customRealm = new CustomRealm();
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

//        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
//        simpleAccountRealm.addAccount("Mark", "123456", "admin", "user");
//        defaultSecurityManager.setRealm(simpleAccountRealm);

        defaultSecurityManager.setRealm(customRealm);
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);
        logger.info("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");
        subject.checkPermission("user:add");
        subject.logout();
        logger.info("isAuthenticated: " + subject.isAuthenticated());
    }


    @Test
    public void testCustomRealmMatcher() {
        //shiro加密

        CustomRealm customRealm = new CustomRealm();
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        //设置加密次数
        matcher.setHashIterations(1);
        //注入到自定义的Realm中
        customRealm.setCredentialsMatcher(matcher);
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);
        logger.info("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");
        subject.checkPermission("user:add");
        subject.logout();
        logger.info("isAuthenticated: " + subject.isAuthenticated());
    }
}
