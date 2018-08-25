package com.liutao.controller;

import com.liutao.model.ResponseModel;
import com.liutao.model.UserModel;
import com.liutao.util.CommonUtil;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/sso/server")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    ConcurrentHashMap<String,Object> userInfoMap = new ConcurrentHashMap<>();
    @GetMapping("/loginPage")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam("username") String username) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("username",username);
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value = "/login-status")
    public ResponseModel createLoginStatus(
            @RequestBody UserModel userModel,
            HttpServletResponse response
            ) {

        logger.debug(userModel.toString());
        ResponseModel responseModel = new ResponseModel(1,"success");
        String ticket = UUID.randomUUID().toString();
        CommonUtil.saveCookie("TICKET",ticket,response);
        saveUserInfo(ticket,userModel);
        return responseModel;
    }

    /**
     * 缓存用户信息
     * @param ticket
     * @param userModel
     */
    public void saveUserInfo(String ticket,UserModel userModel){
        userInfoMap.put(ticket,userModel);
    }
}
