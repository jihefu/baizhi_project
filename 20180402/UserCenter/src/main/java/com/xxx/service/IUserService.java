package com.xxx.service;

import com.xxx.entities.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */
public interface IUserService {
    public User queryUserById(Integer id);
    public void deleteUserById(Integer id);
    public List<User> queryAll();
}
