package com.baizhi.service;

import com.baizhi.dao.UserDAO;
import com.baizhi.entity.User;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDAO userDAO;

    /**
     *  mybatis 一级缓存  有效范围：一次请求有效
     * @return
     */
    @Override
    public List<User> queryAll() {
        List<User> list = userDAO.findAll();
        userDAO.findAll();
        return null;
    }
}
