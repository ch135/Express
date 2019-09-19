package com.express.util;

import java.util.Map;

import javax.servlet.http.*;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * @ClassName Struts2Util
 * @Description 封装了大部分Struts2的方法，用于操作Session,request等方法的工具类 
 * @author jackson
 * @date 2016年3月24日下午11:22:36
 */
public final class Struts2Util {

	private static Map<String,Object> session;
	/**
	 * 得到ActionContext
	 */
	public static ActionContext getContext() {
		return ServletActionContext.getContext();
		
	}
	
	/**
	 * 得到request对象
	 */
	public static HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 得到response对象
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 得到系统获取绝对路径
	 */
	public static String getContextPath() {
		return getRequest().getContextPath();
	}
	
	public static String getRequestURL() {
		return getRequest().getRequestURL().toString();
	}
	
	/**
	 * 得到访问的客户的IP地址 
	 */
	public static String getIp() {
		return getRequest().getRemoteAddr();
	}
	
	/**
	 * 得到Action的application 
	 */
	public static Map<String, Object> getApplication() {
		return getContext().getApplication();
	}

	/**
	 * 得到session
	 * @return
	 */
	public static Map<String, Object> getSession() {
		if(session == null){
			return ActionContext.getContext().getSession();
		}else {
			return session;
		}
	}
	
	/**
	 * 清除map集合的session
	 */
	public static void clearSession() {
		getSession().clear();
	}
	
	/**
	 *	通过key来得到session 
	 */
	public static Object getSession(String key) {
		return getSession().get(key);
	}
	
	/**
	 *	设置session 
	 */
	public static void setSession(String key, Object value) {
		getSession().put(key, value);
	}
	
	public static void removeSession(String key) {
		getSession().remove(key);
	}
	
	
	
}
