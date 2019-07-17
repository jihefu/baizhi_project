package com.baizhi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/4/27.
 */
@Controller
public class HelloController {
    private Logger logger = LoggerFactory.getLogger(HelloController.class);
    /**
     * .ftl
     * @param modelAndView
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(ModelAndView modelAndView){
        logger.info("----------------------");
        modelAndView.setViewName("index");

        HashMap<String, Object> citys = new HashMap<String, Object>();
        citys.put("bj","北京");
        citys.put("tj","天津");

        modelAndView.addObject("citys",citys);

        modelAndView.addObject("name","zs");

        return modelAndView;
    }
}
