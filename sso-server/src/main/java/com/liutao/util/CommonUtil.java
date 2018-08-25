package com.liutao.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: LIUTAO
 * @Date: Created in 2018/8/25  16:01
 * @Modified By:
 */
public class CommonUtil {

    /**
     * 向cookie存值
     * @param key
     * @param value
     * @param response
     */
    public static void saveCookie(String key, String value, HttpServletResponse response){
        Cookie userCookie=new Cookie(key,value);
        userCookie.setMaxAge(30*24*60*60);   //存活期为一个月 30*24*60*60
        userCookie.setPath("/");
        response.addCookie(userCookie);
    }
}
