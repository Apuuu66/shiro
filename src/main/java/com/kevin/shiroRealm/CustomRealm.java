package com.kevin.shiroRealm;

import com.kevin.dao.UserDao;
import com.kevin.pojo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */
public class CustomRealm extends AuthorizingRealm {
    @Resource
    private UserDao userDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        System.out.println("从数据库中获取");
        //通过用户名从数据库或者缓存中获取角色数据
        String username = (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles = userDao.getRolesByUserName(username);
        //通过用户名从数据库或者缓存中获取权限数据
        Set<String> permissions = getPermissionsByUserName(username);

        authorizationInfo.setStringPermissions(permissions);
        authorizationInfo.setRoles(roles);

        return authorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String username) {
        Set<String> sets = new HashSet<>(16);
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    @Override
//    认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的认证信息中，获取用户名
        String username = (String) authenticationToken.getPrincipal();
        //2.通过用户名到数据库中获取凭证
        User user = userDao.getPasswordByUserName(username);
        if (null == user)
            return null;
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("Mark", user.getPassword(), "customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        return authenticationInfo;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456");
        System.out.println(md5Hash.toString());
//        加盐
        Md5Hash md5HashSalt = new Md5Hash("123456", "Mark", 1);
        System.out.println(md5HashSalt.toString());
    }


}
