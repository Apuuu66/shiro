package com.kevin.dao;

import com.kevin.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void getPasswordByUserName(){
        User mark = userDao.getPasswordByUserName("Mark");
        System.out.println(mark);
    }
    @Test
    public void getRolesByUserName(){
        Set<String> mark = userDao.getRolesByUserName("Mark");
        System.out.println(mark);
    }
}
