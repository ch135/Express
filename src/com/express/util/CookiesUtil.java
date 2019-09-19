package com.express.util;

import java.util.Base64;
import java.util.Base64.Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.express.model.User;

/**
 * @ClassName CookiesUtil
 * @Description ���ڴ���cookie�Ĺ����� 
 * @author jackson
 * @date 2016��3��24������11:26:21
 */
public final class CookiesUtil {
	
	/**
	 * ���ڴ���cookie�Զ���¼
	 * @param user
	 * @param res
	 */
	public static void saveCookie(User user,HttpServletResponse res)
	{
		
		long validTime = System.currentTimeMillis() + Constant.COOKIEMAXAGE*1000;
		String cookieValueWithMd5 =MD5Util.getMD5(user.getMobile() + ":" + user.getLoginPassword()+ ":" + validTime + ":" + Constant.WEBKEY);
		String cookieValue = user.getMobile() + ":" + validTime + ":" + cookieValueWithMd5; 
		//String cookieValueBase64 = new String(Base64.encode(cookieValue.getBytes())); 
		Encoder en = Base64.getEncoder();
		String cookieValueBase64 = en.encodeToString(cookieValue.getBytes());
		System.out.println(cookieValueBase64);
		Cookie cookie = new Cookie(Constant.COOKIEDOMAINNAME, cookieValueBase64);

		cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
		
		cookie.setPath("/"); 

		res.addCookie(cookie);
	}
	
	/**
	 * @Title: clearCookie
	 * @Description: ����û���cookie
	 * @param response
	 */
	public static void clearCookie(HttpServletResponse response){
		Cookie cookie = new Cookie(Constant.COOKIEDOMAINNAME, null); 
		cookie.setMaxAge(0); 
		cookie.setPath("/"); 
		response.addCookie(cookie); 
	}
	
}
