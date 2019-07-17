package com.baizhi.service;

import com.xxx.entities.User;
import com.xxx.service.IUserService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */
public class UserService implements IUserService {
    public User queryUserById(Integer integer) {
        System.out.println("===queryUserById===");
        return new User(1,"张小三");
    }

    public void deleteUserById(Integer id) {
        System.out.println("删除用户："+id);
    }

    public List<User> queryAll() {
        return Arrays.asList(new User(1,"zhangsan"),new User(2,"lisi"));
    }
}
