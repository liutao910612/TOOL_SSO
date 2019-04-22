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
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Controller
@RequestMapping("/sso/server")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private ConcurrentHashMap<String, List<String>> ticketMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> ticketAndUsername = new ConcurrentHashMap<>();
    private static final String USERNAME = "LT";
    private static final String PASSWORD = "LT";
    private static final String TGC = "TGC";

    /**
     * 获取的登录页面
     *
     * @return
     */
    @GetMapping("/loginPage")
    public ModelAndView getLoginPage(@RequestParam("service") String service) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Map<String, String> model = new HashMap<>();
        model.put("service", service);
        modelAndView.addAllObjects(model);
        return modelAndView;
    }

    /**
     * 获取首页
     *
     * @param username
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam("username") String username) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("username", username);
        return modelAndView;
    }

    /**
     * 登录
     *
     * @param map
     * @param response
     * @return
     */
    @PostMapping(value = "/login-status")
    @ResponseBody
    public Map<String, Object> createLoginStatus(
            @RequestBody Map<String, String> map,
            HttpServletResponse response
    ) {

        logger.debug(map.toString());
        String service = map.get("service");
        String username = map.get("username");
        String password = map.get("password");

        //用户登录
        Map<String, Object> result = new HashMap<>();
        if(!username.equals(USERNAME) || !password.equals(PASSWORD)){
            result.put("code", 0);
            return result;
        }

        String tgt = UUID.randomUUID().toString();
        CommonUtil.saveCookie(TGC, tgt, response);

        //生成令牌
        String ticket = UUID.randomUUID().toString();
        List<String> tickets = ticketMap.get(tgt);
        if (tickets == null) {
            tickets = new ArrayList<>();
            tickets.add(ticket);
        }

        ticketAndUsername.put(ticket,username);
        result.put("code", 1);
        if(!StringUtils.isEmpty(service)){
            service = service + "?ticket="+ticket;
            result.put("service", service);
        }else{
            result.put("service","index?username="+username);
        }

        return result;
    }

    /**
     * 获取权限接口
     *
     * @param service  客户端地址
     * @param request
     * @return
     */
    @GetMapping("authorization")
    public String getAuth(
            @RequestParam("service") String service,
            HttpServletRequest request
    ) {

        //判断全局会话是否存在，如果存在则获取票据
        String tgc = CommonUtil.getCookieByName(request, TGC);
        HttpSession session = request.getSession();
        String tgt = String.valueOf(session.getAttribute(tgc));

        //如果全局会话存在（票据存在），则生成票据对应的令牌，这里需要注意的是令牌使用一次就失效。这里就不需要从新登录
        if (!StringUtils.isEmpty(tgt)) {
            String ticket = UUID.randomUUID().toString();
            List<String> tickets = ticketMap.get(tgt);
            if (tickets == null) {
                tickets = new ArrayList<>();
                tickets.add(ticket);
            }

            service = service + "?ticket=" + ticket;
            return "redirect:" + service;
        }


        //全局会话不存在，需要重新登录
        String loginUrl = "loginPage?service=" + service;
        return "forward:" + loginUrl;
    }

    @PostMapping("session")
    @ResponseBody
    public String createSession(@RequestParam("ticket") String ticket,
                                            HttpServletRequest request){
        String tgc = CommonUtil.getCookieByName(request, TGC);
        HttpSession session = request.getSession();
        String tgt = String.valueOf(session.getAttribute(tgc));
        List<String> tickets = ticketMap.get(tgt);


        //判断令牌是否有效
        if(tickets.contains(ticket)){
            String username = ticketAndUsername.get(ticket);
            return username;
        }

        return null;

    }

}
