package com.kevin.shiroTest;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */
public class JdbcRealmTest {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro_default?useUnicode=true&useSSL=false&serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    /*    jdbc.driver=com.mysql.cj.jdbc.Driver
        jdbc.url=jdbc:mysql://localhost:3306/erp?useUnicode=true&useSSL=false&serverTimezone=UTC
        jdbc.username=root
        jdbc.password=123456*/
    @Test
    public void testJdbcRealm() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);
        logger.info("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");

        subject.checkPermission("user:delete");
        subject.checkPermission("user:update");


        subject.logout();
        logger.info("isAuthenticated: " + subject.isAuthenticated());
    }

    @Test
    public void testJdbcRealmSQL() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql = "SELECT `password` from test_user WHERE username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("xiongxiong", "123123");
        subject.login(token);
        logger.info("isAuthenticated: " + subject.isAuthenticated());

//        subject.checkRoles("admin", "user");
//
//        subject.checkPermission("user:delete");
//        subject.checkPermission("user:update");


        subject.logout();
        logger.info("isAuthenticated: " + subject.isAuthenticated());
    }
}
