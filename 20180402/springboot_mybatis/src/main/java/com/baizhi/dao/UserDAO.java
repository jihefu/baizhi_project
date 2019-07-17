package com.baizhi.dao;

import com.baizhi.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */
public interface UserDAO {

    public List<User> findAll();
}
