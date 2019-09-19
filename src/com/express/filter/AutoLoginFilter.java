package com.express.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.baidu.mapapi.common.SysOSUtil;
import com.express.model.User;
import com.express.user.service.UserServiceDAO;
import com.express.user.service.impl.UserServiceImpl;
import com.express.util.Constant;
import com.express.util.CookiesUtil;
import com.express.util.MD5Util;
import com.express.util.Struts2Util;

public class AutoLoginFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (Struts2Util.getSession("user") == null) {
			Cookie cookie[] = request.getCookies();
			if (cookie == null) {
				chain.doFilter(request, response);
				return;
			}
			String cookieValue = null;
			for (int i = 0; i < cookie.length; i++) {
				if (Constant.COOKIEDOMAINNAME.equals(cookie[i].getName())) {
					cookieValue = cookie[i].getValue();
					break;
				}
			}

			if (cookieValue == null) {
				chain.doFilter(request, response);
				return;
			}
			String cookieValueDecode = null;
			try {
				Decoder decoder = Base64.getDecoder();
				cookieValueDecode = new String(decoder.decode(cookieValue));
				// cookieValueDecode = new String (Base64.decode(cookieValue));
				String cookieValues[] = cookieValueDecode.split(":");
				if (cookieValues.length != 3) {
					// 非法操作
					System.out.println("非法操作");
					chain.doFilter(request, response);
					return;
				}

				long validTimeInCookie = new Long(cookieValues[1]);
				if (validTimeInCookie < System.currentTimeMillis()) {
					// cookie失效
					CookiesUtil.clearCookie(response);
					chain.doFilter(request, response);
					return;
				}

				String mobile = cookieValues[0];
				if (mobile == null) {
					chain.doFilter(request, response);
					return;
				}
				UserServiceDAO dao = new UserServiceImpl();
				User user = dao.findUserByPhone(mobile);
				if (user == null) {
					chain.doFilter(request, response);
					return;
				}
				if (user != null) {
					String md5ValueInCookie = cookieValues[2];
					String md5ValueFromUser = MD5Util.getMD5(user.getMobile() + ":" + user.getLoginPassword() + ":" + validTimeInCookie + ":" + Constant.WEBKEY);
					if (md5ValueInCookie.equals(md5ValueFromUser)) {
						System.out.println(user.getMobile() + ":" + user.getName());
						request.getSession().setAttribute("user", user);
						chain.doFilter(request, response);
					} else {
						chain.doFilter(request, response);
						return;
					}
				}
			} catch (Exception e) {
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
