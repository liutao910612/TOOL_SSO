package com.liutao.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LIUTAO
 * @Date: Created in 2018/8/25  16:01
 * @Modified By:
 */
public class CommonUtil {

    /**
     * 向cookie存值
     *
     * @param key
     * @param value
     * @param response
     */
    public static void saveCookie(String key, String value, HttpServletResponse response) {
        Cookie userCookie = new Cookie(key, value);
        userCookie.setMaxAge(30 * 24 * 60 * 60);   //存活期为一个月 30*24*60*60
        userCookie.setPath("/");
        response.addCookie(userCookie);
    }


    /**
     * 根据名字获取cookie值
     *
     * @param request
     * @param name    cookie名字
     * @return
     */
    public static String getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
