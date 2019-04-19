package com.liutao.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName="loginFilter",urlPatterns="/*")
public class AuthenticationFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("enter login filter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //判断session中是否存在用户信息,如果存在则直接认证通过
        HttpSession session = req.getSession();
        String username = String.valueOf(session.getAttribute("username"));
        if (!StringUtils.isEmpty(username)) {
            filterChain.doFilter(request, response);
            return;
        }

        //获取请求参数判断是否有ticket,如果有则验证ticket是否有效，并且获取到server返回的用户信息存于session中
        String ticket = request.getParameter("ticket");
        if(!StringUtils.isEmpty(ticket)){
            //去认证中心验证ticket是否有效，并获取到username
            username = "";

            //将username存于session中
            session.setAttribute("username",username);
            filterChain.doFilter(request, response);
            return;
        }



        //如果没有携带ticket并且没有局部会话，则需要重定向到服务端认证，判断是否有全局会话
        String url = ((HttpServletRequest) request).getRequestURI();

        //跳转至sso认证中心
        res.sendRedirect("http://localhost:8001/sso/server/authorization?service="+url);
    }

    @Override
    public void destroy() {

    }
}
