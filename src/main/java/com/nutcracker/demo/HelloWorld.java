package com.nutcracker.demo;

import com.nutcracker.wedo.common.util.PropertyHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by huh on 2016/7/27.
 */
@Slf4j
@Controller
@RequestMapping("/welcome")
public class HelloWorld {

    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Spring 3 MVC Hello World");
        String temp = com.nutcracker.wedo.common.util.PropertyHolder.getContextProperty("domain");
        log.info("你好 hello"+temp+"\n");
        temp = PropertyHolder.getContextProperty("jdbc.url");
        log.info("你好 hello"+temp);
        return "hello";
    }

    @RequestMapping(value="/test")
    public ModelAndView printWelcome(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav= new ModelAndView();
        mav.addObject("city","test");
        mav.setViewName("hello");
        return mav;
    }


}