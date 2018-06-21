package com.kevin.dao;

import com.kevin.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/20
 */
public interface UserDao {
    User getPasswordByUserName(@Param("username") String username);

    Set<String> getRolesByUserName(@Param("username")String username);

//    Set<String> getPermissionsByUserName(String username);
}
