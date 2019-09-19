package com.express.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import com.express.util.Struts2Util;

public class AdminFilter implements Filter {

	private String reLoginPage = "/WEB-INF/TCKD/login.jsp";// 登录失效提示页面

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String uri = request.getRequestURI();
		System.out.println(uri);
		
		// 判断是否有登录
		if (Struts2Util.getSession("username") != null) {
			chain.doFilter(request, response);
		}else if(uri.equals("/Express/admin/Admin_login")){
			System.out.println("用户登录中···");
			chain.doFilter(request, response);
		}else if(uri.equals("/Express/admin/Admin_gologin")){
			System.out.println("跳转到登录页面");
			chain.doFilter(request, response);
		}else if(uri.equals("/Express/admin/Admin_loginOut")){
			System.out.println("退出登录");
			chain.doFilter(request, response);
		}else if(uri.equals("/index.jsp")){
			System.out.println("打开主页");
			chain.doFilter(request, response);
		}else{
			System.out.println(uri);
			System.out.println("进行重定向");
			request.getRequestDispatcher(reLoginPage)
			.forward(request, response);
			return;
		}
		

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
