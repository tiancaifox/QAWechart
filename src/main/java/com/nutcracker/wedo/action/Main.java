package com.nutcracker.wedo.action;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理首页
 * Created by huh on 2017/2/21.
 */
@Controller
@RequestMapping("/main")
public class Main {

    /**
     * 进入首页
     *
     * @return vm
     */
    @RequestMapping(value="/index",method = RequestMethod.GET)
    public ModelAndView initPage() {
        Map<String, Object> model = new HashMap<String, Object>();
        /*model.put("accountId", SessionUtil.getAccountWrap().getAccount().getId());
        model.put("accountName", SessionUtil.getAccountWrap().getAccount().getName());*/
        model.put("accountId", "3");
        model.put("accountName", "huhao06");
        ModelAndView v = new ModelAndView("/main/main", model);
        return v;
    }
}

