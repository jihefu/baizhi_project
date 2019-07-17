package com.baizhi.demo;

import com.baizhi.entity.Attribute;
import com.baizhi.entity.Order;
import com.baizhi.entity.User;
import com.mongodb.WriteResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestMongoTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Test
    public void save(){
        User user=new User("lisi",new Date(),false);
        mongoTemplate.save(user);
        System.out.println(user);
    }
    @Test
    public void save2(){
        User user=new User("ww",new Date(),false);
        List<Attribute> attributes= Arrays.asList(new Attribute("age",18),
                new Attribute("address","北京上地"));
        user.setAttributes(attributes);
        mongoTemplate.save(user);
        System.out.println(user);
    }
    @Test
    public void save3(){
        User user=new User("win7",new Date(),false);
        List<Attribute> attributes= Arrays.asList(new Attribute("age",18),
                new Attribute("address","北京上地"));
        List<Order> orders= Arrays.asList(new Order("订单1",9.8),
                new Order("订单2",5.5));
        user.setAttributes(attributes);
        user.setOrders(orders);
        for (Order order : orders) {
            mongoTemplate.save(order);
        }
        mongoTemplate.save(user);
        System.out.println(user);
    }
    @Test
    public void query3(){
        User user = mongoTemplate.findById("5acc8956514426327033b8c4", User.class);
        for (Attribute attribute : user.getAttributes()) {
            System.out.println(attribute);
        }
        for (Order order : user.getOrders()) {
            System.out.println(order);
        }
    }
    @Test
    public void query1(){
        Query query=new Query();
        List<User> users = mongoTemplate.find(query, User.class);
        for (User user : users) {
            System.out.println(user);
        }
    }
    @Test
    public void query2(){
        Query query=new Query();

        //构建查询项
        Criteria criteria=new Criteria();
        criteria.orOperator(new Criteria("tname").is("zhansan"),
                new Criteria("sex").is(false));

        query.addCriteria(criteria);

        System.out.println(query.getQueryObject());

        List<User> users = mongoTemplate.find(query, User.class);
        for (User user : users) {
            System.out.println(user);
        }
    }
    @Test
    public void update() {
        Query query=new Query();
        Update update=new Update();
        update.inc("salary",1000.0f);
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, User.class);
        System.out.println(writeResult);
    }
    @Test
    public void delete() {
        WriteResult remove = mongoTemplate.remove(new Query(), User.class);
        System.out.println(remove);
    }
}
