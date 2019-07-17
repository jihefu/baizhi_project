package com.baizhi.service;

import com.baizhi.entities.Attribute;
import com.baizhi.entities.User;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */
public interface IUserService {
    /**
     * 用户注册接口
     * @param user
     */
    public void registerUser(User user);
    /**
     * 用户登录接口
     * @param user
     */
    public User userLogin(User user);

    /**
     * 更新用户的属性 有就修改，没有就添加
     * @param id
     * @param attribute
     * @throws IllegalAccessException
     */
    public void updateAttribute(String id, Attribute attribute) throws IllegalAccessException;

    /**
     * 删除用户的属性
     * @param id
     * @param attribute
     * @throws IllegalAccessException
     */
    public void deleteAttribute(String id, String attribute) throws IllegalAccessException;

    /**
     * 用户 id 删除接口
     * @param
     */
    public void deleteUserById(String id);

    /**
     * 按照 attribute 删除
     * @param attribute
     */
    public void deleteUserByAttribute(Attribute... attribute);


    /**
     * 用户 ID 查询接口
     * @param id
     * @return
     */
    public User queryUserById(String id);
    /**
     * 分页查询
     * @param pageNow
     * @param pageSize
     * @param attributes
     * @return
     */
    public List<User> queryByAttributes(int pageNow, int pageSize, Attribute... attributes);
}
