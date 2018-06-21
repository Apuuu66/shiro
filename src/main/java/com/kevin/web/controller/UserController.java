package com.kevin.web.controller;

import com.kevin.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/10
 */
@Controller
public class UserController {
    @RequestMapping(value = "/subLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            token.setRememberMe(user.getRememberMe());
            subject.login(token);
            if (subject.hasRole("admin")) {
                return "有admin权限";
            } else return "无admin权限";

        } catch (AuthenticationException e) {
            return e.getMessage();
        }
//        return "登陆成功";
    }

    //具备admin角色才能访问该方法
    @RequiresRoles("admin")
    @RequestMapping("/testRole")
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    //    @RequiresRoles("admin1")
    @RequestMapping("/testRole1")
    @ResponseBody
    public String testRole1() {
        return "testRole success";
    }

    //具备该权限的主体才能访问该方法
//    @RequiresPermissions("XXX")
    @RequestMapping("/testPerms")
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    @RequestMapping("/testPerms1")
    @ResponseBody
    public String testPerms1() {
        return "testPerms1 success";
    }

    @RequestMapping("/testPerms2")
    @ResponseBody
    public String testPerms2() {
        return "testPerms2 success";
    }

    @RequestMapping("/testRole2")
    @ResponseBody
    public String testRole2() {
        return "testRole2 success";
    }
}
