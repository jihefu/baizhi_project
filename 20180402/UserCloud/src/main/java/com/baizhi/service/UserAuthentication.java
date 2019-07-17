package com.baizhi.service;
import com.baizhi.entities.Ticket;
import com.baizhi.entities.Token;

/**
 * Created by Administrator on 2018/4/12.
 */
public interface UserAuthentication {
    /**
     * 生成令牌 将生成密文返回给 应用，系统同时存储 Token对象 （明文） -- redis
     *
     {name、password、lastUpdate、expire}  --加密算法--> 密文

     Redis 存储：
      Key       value
     密文  --- Token：{name、password、lastUpdate、expire}

     * @param name
     * @param password
     * @param appID
     * @return :加密后的Token对象
     */
    public String authenticate(String name, String password, String appID);

    /**
     * 校验 令牌时效性
     * @param token
     * @param appID
     * @return
     */
    public boolean tokenValidate(String token,String appID);

    /**
     * 根据 传入信息查询业务数据
     * @param name
     * @param password
     * @param appID
     * @return
     */
    public Ticket  ticketGrant(String name, String password,String appID);

    /**
     * 退出，销毁令牌、应用系统选择性销毁 ticket
     * @param token
     * @param appID
     */
    public void logout(String token,String appID);
}
