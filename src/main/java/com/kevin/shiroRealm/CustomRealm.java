package com.kevin.shiroRealm;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */
public class CustomRealm extends AuthorizingRealm {
    Map<String, String> userMap = new HashMap<>();

    {
//        userMap.put("Mark", "123456");

        //设置加密后的密码
//        userMap.put("Mark", "e10adc3949ba59abbe56e057f20f883e");
        //设置加盐后的密码
        userMap.put("Mark", "283538989cef48f3d7d8a1c1bdf2008f");

        super.setName("customRealm");
    }

    @Override
    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //通过用户名从数据库或者缓存中获取角色数据
        String username = (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByUserName(username);

        //通过用户名从数据库或者缓存中获取权限数据
        Set<String> permissions = getPermissionsByUserName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
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

    private Set<String> getRolesByUserName(String username) {
        Set<String> sets = new HashSet<>(16);
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    @Override
//    认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的认证信息中，获取用户名
        String username = (String) authenticationToken.getPrincipal();
        //2.通过用户名到数据库中获取凭证
        String password = getPassWordByUserName(username);
        if (null == password)
            return null;
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("Mark", password, "customRealm");

        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        return authenticationInfo;
    }

    //模拟数据库查询凭证
    private String getPassWordByUserName(String username) {

        return userMap.get(username);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456");
        System.out.println(md5Hash.toString());

//        加盐
        Md5Hash md5HashSalt = new Md5Hash("123456", "Mark",1);
        System.out.println(md5HashSalt.toString());
    }


}
