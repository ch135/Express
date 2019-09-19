package com.express.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;


/**
 * 丁冬云的短信验证码
 * @author LK
 *
 */
public class dingdongyunMsgUtil {

	   /**
  * 发送验证码短信
  * 
  * @param apikey
  *            apikey
  * @param mobile
  *            手机号码(唯一，不许多个)
  * @param content
  *            短信发送内容（必须经过utf-8格式编码)
  * @return json格式字符串
  */
	public static String msg(String mobile){
		String URL_SEND_YZM ="https://api.dingdongcloud.com/v1/sms/sendyzm";
		String ENCODING = "UTF-8";
		String apikey = Constant.MSGAPIKEY;
		Random random = new Random();  
		int randomNumber =  (random.nextInt(89999) + 10000);
		String code = Integer.toString(randomNumber);
		String yzmContent = getMsg()+code; 
		 if (StringUtils.isNotBlank(yzmContent)) {
	            try {
	            	yzmContent = URLEncoder.encode(yzmContent, ENCODING);
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	        }
	 
	        NameValuePair[] data = { 
	        new NameValuePair("apikey", apikey),
	 
	        new NameValuePair("mobile", mobile),
	 
	        new NameValuePair("content", yzmContent) };
	 
	        doPost(URL_SEND_YZM, data);
	    
		return code;
		
	};
	
	/**
  * 基于HttpClient的post函数
  * 
  * @param url
  *            提交的URL
  * 
  * @param data
  *            提交NameValuePair参数
  * @return 提交响应
  */
 private static String doPost(String url, NameValuePair[] data) {

     HttpClient client = new HttpClient();
     PostMethod method = new PostMethod(url);
     // method.setRequestHeader("ContentType",
     // "application/x-www-form-urlencoded;charset=UTF-8");
     method.setRequestBody(data);
     // client.getParams().setContentCharset("UTF-8");
     client.getParams().setConnectionManagerTimeout(10000);
     try {
         client.executeMethod(method);
         return method.getResponseBodyAsString();
     } catch (Exception e) {
         e.printStackTrace();
     }
     return null;
 }
 
 /**
  * 获得当前验证码模版
  * @return
  */
 public static String getMsg()
 {
	 return Constant.MSG;
 }
 
 	/**
 	 * 修改短信模版
 	 * @param msg
 	 */
 	public static void ChangeMsg(String msg)
 	{
 		Constant.MSG=msg;
 	}
}
