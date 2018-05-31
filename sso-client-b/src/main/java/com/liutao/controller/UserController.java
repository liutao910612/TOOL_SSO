package com.liutao.controller;

import com.liutao.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户控制层
 *
 * @author LIUTAO
 * @version 2017/3/29
 * @see
 * @since
 */
@Controller
@RequestMapping("/liutao/v1")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView getUserInfo() {
        logger.debug("enter index");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","熊大");
        modelAndView.setViewName("index");
        CommonUtil.getUUID();
        return modelAndView;
    }


}
