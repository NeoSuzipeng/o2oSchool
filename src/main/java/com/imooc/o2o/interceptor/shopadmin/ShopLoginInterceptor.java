package com.imooc.o2o.interceptor.shopadmin;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.imooc.o2o.entity.PersonInfo;

/**
 * 店鋪登录拦截器
 * 用于拦截试图非法获取店铺管理员权限
 * 将请求定向到登录页面
 * @author 10353
 *
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter{
    
	private static final Logger log = LoggerFactory.getLogger(ShopLoginInterceptor.class);
	
	@Override
	/**
	 * 通过判断会话中是否存在用户信息来进行检验权限
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        
        boolean notEmptyUserInfo =  null != user && null != user.getUserId() 
        		&& user.getEnableStatus() == 1 && user.getUserId() > 0;
        //如果验证通过继续前往请求页面
        if(notEmptyUserInfo) {
        	log.info("请求到来");
        	return true;
        }
        //如果不通过验证重定向到登录页面
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open('"+request.getContextPath()+"/local/login?usertype=2','_self')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}
}
