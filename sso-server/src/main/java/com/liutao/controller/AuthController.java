package com.liutao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sso/server")
public class AuthController {

    private Map<String,String> tokenMap = new HashMap<>();  //存储token对象

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @GetMapping("/authorization")
    public ModelAndView getLoginPage(HttpServletRequest httpServletRequest){

        //获取到sessionId
        String sessionId = httpServletRequest.getSession().getId();
        logger.debug("sessionId:"+sessionId);

        //判断浏览器是否已经和sso server建立了会话
        String token = tokenMap.get(sessionId);
        if(StringUtils.isEmpty(token)){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login");
            return modelAndView;
        }

        //TODO 重定向到请求地址
        return null;

    }
}
