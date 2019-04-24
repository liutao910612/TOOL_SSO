package com.liutao.sso.client.filter;
import com.liutao.sso.client.util.MiniHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebFilter(filterName="loginFilter",urlPatterns="/*")
public class AuthenticationFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private String authorizationUrl;

    private String verifyTicketUrl;

    @Autowired
    private Environment environment;

    private final static  String USERNAME = "username";


    @Override
    public void init(FilterConfig filterConfig) {
        authorizationUrl = environment.getProperty("sso.service.authorization");
        verifyTicketUrl = environment.getProperty("sso.service.session");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("enter login filter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        if (!StringUtils.isEmpty(checkLocalSession(session))) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * 到这里说明局部会话没有创建
         * 获取请求参数判断是否有ticket,如果有则验证ticket是否有效，并且获取到server返回的用户信息存于session中
         */
        String ticket = request.getParameter("ticket");
        if(!StringUtils.isEmpty(ticket)){
            //去认证中心验证ticket是否有效，并获取到username
            String username = MiniHttpClient.post(verifyTicketUrl+"?ticket="+ticket,new HashMap<String, String>());

            if(!StringUtils.isEmpty(username)){
                //将username存于session中
                session.setAttribute("username",username);
                filterChain.doFilter(request, response);
                return;
            }
        }

        /**
         * 满足以下情况中的任意一个就进入下面的步骤。
         * （1）针对局部会话不存在，并且没有携带ticket
         * （2）针对局部会话不存在，并且携带的ticket无效
         *
         * 重定向到服务端认证，判断是否有全局会话
         */
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        res.sendRedirect(authorizationUrl+"?service=" + url);
    }

    @Override
    public void destroy() {

    }

    /**
     * 获取session中的username,如果username存在则说明局部会话存在
     * @return
     */
    private String checkLocalSession(HttpSession session){
        Object obj = session.getAttribute(USERNAME);

        String username = obj == null ? null : (String)obj;
        return username;
    }
}
