package com.kevin.shiroTest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */
public class IniRealmTest {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    @Test
    public void testIniRealm() {
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);
        logger.info("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin");

        subject.checkPermission("user:delete");
        subject.checkPermission("user:update");


        subject.logout();
        logger.info("isAuthenticated: " + subject.isAuthenticated());
    }
}
