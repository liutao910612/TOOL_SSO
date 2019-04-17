package com.liutao.controller;

import com.liutao.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Controller
@RequestMapping("/sso/server")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    ConcurrentHashMap<String,Object> userInfoMap = new ConcurrentHashMap<>();
    private static final String TICKET = "TICKET";

    /**
     * 获取的登录页面
     * @return
     */
    @GetMapping("/loginPage")
    public ModelAndView getLoginPage(@RequestParam("url")String url) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Map<String, String> model = new HashMap<>();
        model.put("url", url);
        modelAndView.addAllObjects(model);
        return modelAndView;
    }

    /**
     * 获取首页
     * @param username
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam("username") String username) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("username",username);
        return modelAndView;
    }

    /**
     * 登录
     * @param map
     * @param response
     * @return
     */
    @PostMapping(value = "/login-status")
    @ResponseBody
    public Map<String,Object> createLoginStatus(
            @RequestBody Map<String,String> map,
            HttpServletResponse response
            ) {

        logger.debug(map.toString());
        String ticket = UUID.randomUUID().toString();
        CommonUtil.saveCookie(TICKET,ticket,response);
        map.remove("url");
        saveUserInfo(ticket,map);
        Map<String,Object> result = new HashMap<>();
        result.put("code",1);
        result.put("username",map.get("username"));
        return result;
    }

    /**
     * 获取权限接口
     * @param url 客户端地址
     * @param request
     * @return
     */
    @GetMapping("authorization")
    public String getAuth(
            @RequestParam("url")String url,
            HttpServletRequest request
    ){

        //判断全局会话是否存在
        String ticket = CommonUtil.getCookieByName(request,TICKET);

        //全局会话不存在，需要重新登录
        if(StringUtils.isEmpty(ticket)){
            String loginUrl = "loginPage?url="+url;
            return "forward:"+loginUrl;
        }

        //会话存在，不需要重新登录，跳转到客户端地址
        url = url +"?ticket="+ticket;
        return "redirect:"+url;
    }

    /**
     * 缓存用户信息
     * @param ticket
     * @param object
     */
    public void saveUserInfo(String ticket,Object object){
        userInfoMap.put(ticket,object);
    }
}
