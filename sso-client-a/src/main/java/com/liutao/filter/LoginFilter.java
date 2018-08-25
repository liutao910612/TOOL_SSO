package com.liutao.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName="loginFilter",urlPatterns="/*")
public class LoginFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("enter login filter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Object isLogin = session.getAttribute("isLogin");
        if ( isLogin != null && (Boolean)isLogin == true) {
            filterChain.doFilter(request, response);
            return;
        }
        //跳转至sso认证中心
        res.sendRedirect("http://localhost:8001/sso/server/authorization");
    }

    @Override
    public void destroy() {

    }
}
