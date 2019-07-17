package com.baizhi.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2018/4/25.
 */
public class LogDemo {
    public static void main(String[] args) {
        Log log= LogFactory.getLog(LogDemo.class);
        log.info("haha！");
    }
}
